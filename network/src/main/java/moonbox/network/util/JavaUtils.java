package moonbox.network.util;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {

  private static final ImmutableMap<String, ByteUnit> byteSuffixes =
          ImmutableMap.<String, ByteUnit>builder()
                  .put("b", ByteUnit.BYTE)
                  .put("k", ByteUnit.KiB)
                  .put("kb", ByteUnit.KiB)
                  .put("m", ByteUnit.MiB)
                  .put("mb", ByteUnit.MiB)
                  .put("g", ByteUnit.GiB)
                  .put("gb", ByteUnit.GiB)
                  .put("t", ByteUnit.TiB)
                  .put("tb", ByteUnit.TiB)
                  .put("p", ByteUnit.PiB)
                  .put("pb", ByteUnit.PiB)
                  .build();

  public static ByteBuf stringToBytes(String s) {
    return Unpooled.wrappedBuffer(s.getBytes(StandardCharsets.UTF_8));
  }

  public static String bytesToString(ByteBuf b) {
    return b.toString(StandardCharsets.UTF_8);
  }

  public static byte[] byteBufToByteArray(ByteBuf buf) {
    byte[] array;
    int length = buf.readableBytes();
    if (buf.hasArray()) {
      array = buf.array();
    } else {
      array = new byte[length];
          buf.getBytes(buf.readerIndex(), array, 0, length);
    }
    return array;
  }

  /**
   * Convert a passed byte string (e.g. 50b, 100kb, or 250mb) to the given. If no suffix is
   * provided, a direct conversion to the provided unit is attempted.
   */
  public static long byteStringAs(String str, ByteUnit unit) {
    String lower = str.toLowerCase(Locale.ROOT).trim();

    try {
      Matcher m = Pattern.compile("([0-9]+)([a-z]+)?").matcher(lower);
      Matcher fractionMatcher = Pattern.compile("([0-9]+\\.[0-9]+)([a-z]+)?").matcher(lower);

      if (m.matches()) {
        long val = Long.parseLong(m.group(1));
        String suffix = m.group(2);

        // Check for invalid suffixes
        if (suffix != null && !byteSuffixes.containsKey(suffix)) {
          throw new NumberFormatException("Invalid suffix: \"" + suffix + "\"");
        }

        // If suffix is valid use that, otherwise none was provided and use the default passed
        return unit.convertFrom(val, suffix != null ? byteSuffixes.get(suffix) : unit);
      } else if (fractionMatcher.matches()) {
        throw new NumberFormatException("Fractional values are not supported. Input was: "
                + fractionMatcher.group(1));
      } else {
        throw new NumberFormatException("Failed to parse byte string: " + str);
      }

    } catch (NumberFormatException e) {
      String byteError = "Size must be specified as bytes (b), " +
              "kibibytes (k), mebibytes (m), gibibytes (g), tebibytes (t), or pebibytes(p). " +
              "E.g. 50b, 100k, or 250m.";

      throw new NumberFormatException(byteError + "\n" + e.getMessage());
    }
  }

  /**
   * Convert a passed byte string (e.g. 50b, 100k, or 250m) to bytes for
   * internal use.
   *
   * If no suffix is provided, the passed number is assumed to be in bytes.
   */
  public static long byteStringAsBytes(String str) {
    return byteStringAs(str, ByteUnit.BYTE);
  }

  /**
   * Convert a passed byte string (e.g. 50b, 100k, or 250m) to kibibytes for
   * internal use.
   *
   * If no suffix is provided, the passed number is assumed to be in kibibytes.
   */
  public static long byteStringAsKb(String str) {
    return byteStringAs(str, ByteUnit.KiB);
  }

  /**
   * Convert a passed byte string (e.g. 50b, 100k, or 250m) to mebibytes for
   * internal use.
   *
   * If no suffix is provided, the passed number is assumed to be in mebibytes.
   */
  public static long byteStringAsMb(String str) {
    return byteStringAs(str, ByteUnit.MiB);
  }

  /**
   * Convert a passed byte string (e.g. 50b, 100k, or 250m) to gibibytes for
   * internal use.
   *
   * If no suffix is provided, the passed number is assumed to be in gibibytes.
   */
  public static long byteStringAsGb(String str) {
    return byteStringAs(str, ByteUnit.GiB);
  }

}
