package riot.api.data.engineer.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateValid {

    String message() default "yyyy-mm-dd 형식어야합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern() default "yyyy-MM-dd";
}
