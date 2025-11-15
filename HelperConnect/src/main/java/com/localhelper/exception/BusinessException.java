package com.localhelper.exception;

public class BusinessException extends RuntimeException {
    
    private String errorCode;
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

// Specific business exceptions
class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super("USER_NOT_FOUND", message);
    }
}

class HelperNotFoundException extends BusinessException {
    public HelperNotFoundException(String message) {
        super("HELPER_NOT_FOUND", message);
    }
}

class ServiceRequestNotFoundException extends BusinessException {
    public ServiceRequestNotFoundException(String message) {
        super("SERVICE_REQUEST_NOT_FOUND", message);
    }
}

class PaymentNotFoundException extends BusinessException {
    public PaymentNotFoundException(String message) {
        super("PAYMENT_NOT_FOUND", message);
    }
}

class ReviewNotFoundException extends BusinessException {
    public ReviewNotFoundException(String message) {
        super("REVIEW_NOT_FOUND", message);
    }
}

class ComplaintNotFoundException extends BusinessException {
    public ComplaintNotFoundException(String message) {
        super("COMPLAINT_NOT_FOUND", message);
    }
}

class UnauthorizedAccessException extends BusinessException {
    public UnauthorizedAccessException(String message) {
        super("UNAUTHORIZED_ACCESS", message);
    }
}

class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(String message) {
        super("DUPLICATE_RESOURCE", message);
    }
}

class InvalidOperationException extends BusinessException {
    public InvalidOperationException(String message) {
        super("INVALID_OPERATION", message);
    }
}

class PaymentProcessingException extends BusinessException {
    public PaymentProcessingException(String message) {
        super("PAYMENT_PROCESSING_ERROR", message);
    }
}