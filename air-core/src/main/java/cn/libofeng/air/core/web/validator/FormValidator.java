/*
 * 版权所有: 版权所有 (C) 2014-2024
 * 公    司: 北京帮付宝网络科技有限公司
 *
 * FormValidator
 *
 * 创建时间: 2015-02-26 10:14
 *
 * 作者: 李柏锋 (工号: AB044566)
 */

package cn.libofeng.air.core.web.validator;

import cn.libofeng.air.core.web.form.ControllerForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 功能描述:
 * <p>基础表单验证器</p>
 * <p>版本: 1.0.0</p>
 */
public abstract class FormValidator<T extends ControllerForm> implements Validator {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Can this {@link org.springframework.validation.Validator} {@link #validate(Object, org.springframework.validation.Errors) validate}
     * instances of the supplied {@code clazz}?
     * <p>This method is <i>typically</i> implemented like so:
     * <pre class="code">return Foo.class.isAssignableFrom(clazz);</pre>
     * (Where {@code Foo} is the class (or superclass) of the actual
     * object instance that is to be {@link #validate(Object, org.springframework.validation.Errors) validated}.)
     *
     * @param clazz the {@link Class} that this {@link org.springframework.validation.Validator} is
     *              being asked if it can {@link #validate(Object, org.springframework.validation.Errors) validate}
     * @return {@code true} if this {@link org.springframework.validation.Validator} can indeed
     * {@link #validate(Object, org.springframework.validation.Errors) validate} instances of the
     * supplied {@code clazz}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return this.getClass().isAssignableFrom(clazz);
    }

    /**
     * Validate the supplied {@code target} object, which must be
     * of a {@link Class} for which the {@link #supports(Class)} method
     * typically has (or would) return {@code true}.
     * <p>The supplied {@link org.springframework.validation.Errors errors} instance can be used to report
     * any resulting validation errors.
     *
     * @param target the object that is to be validated (can be {@code null})
     * @param errors contextual state about the validation process (never {@code null})
     */
    @Override
    @SuppressWarnings("unchecked")
    public void validate(Object target, Errors errors) {
        validate((T) target, errors);
    }

    public abstract void validate(T form, Errors errors);
}