package moonbox.mbw.annotation;

import java.lang.annotation.*;

/**
 * JsonParam Annotation
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonParam {
    String value();
}
