package moonbox.jdbc.util;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import moonbox.jdbc.MbDriver;
import moonbox.network.util.JavaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

public class Utils {

    public static ByteBuf messageToByteBuf(Message message) {
        return Unpooled.wrappedBuffer(message.toByteArray());
    }

    public static byte[] byteBufToByteArray(ByteBuf buf) {
        return JavaUtils.byteBufToByteArray(buf);
    }

    public static Properties parseURL(String url, Properties info) {
        String stripPrefix = url.substring(MbDriver.URL_PREFIX.length());
        String[] split = stripPrefix.split("\\?");
        String[] hostPortAndDb = split[0].split("/");
        String[] hostAndPort = hostPortAndDb[0].split(":");

        info.put("host", hostAndPort[0].trim());

        if (hostAndPort.length > 1) {
            info.put("port", Integer.valueOf(hostAndPort[1]));
        }

        if (hostPortAndDb.length > 1) {
            info.put("database", hostPortAndDb[1].trim());
        }

        if (split.length > 1) {
            String[] param = split[1].split("&");
            for (int i = 0; i < param.length; i++) {
                String[] kv = param[i].split("=");
                assert kv.length == 2;
                info.putIfAbsent(kv[0].trim(), kv[1].trim());
            }
        }

        // key to lower case
        Properties properties = new Properties();
        info.entrySet().forEach(entry -> {
            String key = entry.getKey().toString().toLowerCase();
            properties.put(key, entry.getValue());
        });

        return properties;
    }

    public static List<String> splitSQLs(String sql) {
        Stack<Character> stack = new Stack<>();
        List<Integer> splitIndexes = new ArrayList<>();
        char[] chars = sql.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ';') {
                if (stack.isEmpty() && i != (chars.length - 1)) {
                    splitIndexes.add(i);
                }
            }
            if (chars[i] == '(') {
                stack.push('(');
            }
            if (chars[i] == ')') {
                stack.pop();
            }
        }
        return split(sql, splitIndexes);
    }

    private static List<String> split(String sql, List<Integer> index) {
        List<String> result = new ArrayList<>();
        if (index.isEmpty()) {
            result.add(sql);
        } else {
            int begin = 0;
            for (int i = 0; i < index.size(); i++) {
                int point = index.get(i);
                result.add(sql.substring(begin, point));
                begin = point + 1;
            }
            if (begin != sql.length() - 1) {
                String lastSql = sql.substring(begin, sql.length());
                if (!lastSql.matches("\\s+"))
                    result.add(lastSql);
            }
        }
        return result;
    }

}
