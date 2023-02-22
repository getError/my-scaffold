package com.sankuai.blue.infra.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sankuai.blue.feature.common.util.JsonUtils;
import com.sankuai.blue.infra.exception.InternalException;
import org.apache.commons.text.translate.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author hanyecong02
 */
public abstract class StringListEncode {

    private static final CharSequenceTranslator ESCAPE_STRING_LIST;
    static {
        Map<String, String> escapeStringListMap = new HashMap<>();
        escapeStringListMap.put("\\", "\\\\");
        escapeStringListMap.put(",", "\\,");
        ESCAPE_STRING_LIST = new AggregateTranslator(
                new LookupTranslator(Collections.unmodifiableMap(escapeStringListMap)),
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE),
                JavaUnicodeEscaper.outsideOf(32, 0x7f)
        );
    }

    private static final CharSequenceTranslator UNESCAPE_STRING_LIST;
    static {
        Map<String, String> unescapeStringListMap = new HashMap<>();
        unescapeStringListMap.put("\\\\", "\\");
        unescapeStringListMap.put("\\,", ",");
        UNESCAPE_STRING_LIST = new AggregateTranslator(
                new OctalUnescaper(),
                new UnicodeUnescaper(),
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE),
                new LookupTranslator(Collections.unmodifiableMap(unescapeStringListMap))
        );
    }

    private final static TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<List<String>>() {};

    private StringListEncode() {}

    public static String encodeStringList(List<String> value) {

        StringBuilder sb = new StringBuilder();

        boolean tailIsEmpty = false;

        for (String item : value) {

            if (item == null) {
                throw new InternalException("Can't escape null String in List<String>.");
            }

            if (sb.length() > 0) {
                sb.append(',');
            }

            String escaped = ESCAPE_STRING_LIST.translate(item);
            sb.append(escaped);

            tailIsEmpty = escaped.length() == 0;
        }

        if (tailIsEmpty) {
            sb.append(",");
        }

        return sb.toString();
    }

    public static List<String> decodeStringList(String value) {

        if (value == null) {
            return null;
        } else if (StringUtils.isEmpty(value)) {
            return new ArrayList<>();
        }

        // 历史Json数据
        if (value.startsWith("[") && JsonUtils.isJson(value)) {
            try {
                return JsonUtils.getMapper().readValue(value, STRING_LIST_TYPE);
            } catch (IOException e) {
                throw new InternalException("Error while decode json List<String>: " + value, e);
            }
        }

        final List<String> result = new ArrayList<>();

        final int len = value.length();
        StringWriter writer = new StringWriter(value.length());

        try {

            int pos = 0;
            while (pos < len) {

                final int consumed = UNESCAPE_STRING_LIST.translate(value, pos, writer);
                final char c1 = value.charAt(pos);

                if (consumed == 0 && c1 != ',') {

                    int codePoint = Character.codePointAt(value, pos);
                    char[] chars = Character.toChars(codePoint);
                    writer.write(chars);

                    pos += chars.length;
                    continue;
                }

                // contract with translators is that they have to understand codepoints
                // and they just took care of a surrogate pair
                for (int pt = 0; pt < consumed; pt++) {
                    pos += Character.charCount(Character.codePointAt(value, pos));
                }

                //noinspection ConstantConditions
                if (consumed == 0 && c1 == ',') {
                    result.add(writer.toString());
                    writer = new StringWriter(value.length());
                    pos++;
                }
            }
        } catch (IOException e) {
            throw new InternalException("Error while decode encoded List<String>: " + value, e);
        }

        result.add(writer.toString());

        if (result.size() >= 2) {

            String tail = result.get(result.size() - 1);
            String lastTail = result.get(result.size() - 2);

            if (StringUtils.isEmpty(tail) && StringUtils.isEmpty(lastTail)) {
                result.remove(result.size() - 1);
            }
        }

        return result;
    }

    public static String regexpContains(String item) {

        String stringListEscaped = ESCAPE_STRING_LIST.translate(item);
        String regexpEscaped = Pattern.quote(stringListEscaped);

        return "(^|,)" + regexpEscaped + "(,|$)";
    }
}
