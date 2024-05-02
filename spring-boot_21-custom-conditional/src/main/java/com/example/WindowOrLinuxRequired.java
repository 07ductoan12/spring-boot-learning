package com.example;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.context.annotation.Conditional;

/** WindowOrLinuxRequired */
public class WindowOrLinuxRequired extends AnyNestedCondition {
    public WindowOrLinuxRequired() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @Conditional(WindowRequired.class)
    public class RunOnWindow {}

    @Conditional(LinuxRequired.class)
    public class RunOnLinux {}
}
