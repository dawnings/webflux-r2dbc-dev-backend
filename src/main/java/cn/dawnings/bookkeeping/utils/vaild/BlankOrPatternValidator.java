//package cn.dawnings.bookkeeping.utils.vaild;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//import javax.validation.constraints.Pattern.Flag;
//import java.util.regex.Matcher;
//
///**
// * 验证器  当字段不为null且长度>0  才执行正则校验
// */
//public class BlankOrPatternValidator implements ConstraintValidator<BlankOrPattern, String> {
//    private java.util.regex.Pattern pattern;
//
//    public BlankOrPatternValidator() {
//    }
//
//    @Override
//    public void initialize(BlankOrPattern parameters) {
//        Flag[] flags = parameters.flags();
//        int intFlag = 0;
//
//        for (Flag flag : flags) {
//            intFlag |= flag.getValue();
//        }
//
//        this.pattern = java.util.regex.Pattern.compile(parameters.regexp(), intFlag);
//    }
//
//    @Override
//    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
//        if (value == null || value.length() == 0) {
//            return true;
//        } else {
//            Matcher m = this.pattern.matcher(value);
//            return m.matches();
//        }
//    }
//}
