package net.skhu.wassup.app.attendance.domain;

import net.skhu.wassup.global.error.ErrorCode;
import net.skhu.wassup.global.error.exception.CustomException;

public enum Status {
    ATTENDANCE, ABSENCE, ILLNESS, LEAVING;

    public static Status of(int statusId) {
        for (Status status : Status.values()) {
            if (status.ordinal() == statusId) {
                return status;
            }
        }
        throw new CustomException(ErrorCode.NOT_FOUND_ATTENDANCE_STATUS);
    }
}
