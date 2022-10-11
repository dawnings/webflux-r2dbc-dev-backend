//package cn.dawnings.bookkeeping.utils.vaild;
//
//import javax.validation.Constraint;
//import javax.validation.Payload;
//import javax.validation.constraints.Pattern;
//import java.lang.annotation.*;
//
///**自定义注解
// * 验证器  当 当字段不为null且长度>0  才执行正则校验
// */
//@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Constraint(validatedBy = {BlankOrPatternValidator.class})
//public @interface BlankOrPattern {
//    String regexp();
//
//    Pattern.Flag[] flags() default {};
//
//    String message() default "{javax.validation.constraints.Pattern.message}";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
//    @Retention(RetentionPolicy.RUNTIME)
//    @Documented
//    @interface List {
//        Pattern[] value();
//    }
//    enum Flag {
//        UNIX_LINES(1),
//        CASE_INSENSITIVE(2),
//        COMMENTS(4),
//        MULTILINE(8),
//        DOTALL(32),
//        UNICODE_CASE(64),
//        CANON_EQ(128);
//        private final int value;
//        Flag(int value) {
//            this.value = value;
//        }
//        public int getValue() {
//            return this.value;
//        }
//    }
//}
