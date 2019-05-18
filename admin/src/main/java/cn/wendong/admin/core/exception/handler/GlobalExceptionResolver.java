package cn.wendong.admin.core.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import cn.wendong.admin.common.enums.ErrorCodeEnum;
import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;

/**
 * 全局异常处理 主要处理ajax请求
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-14
 */
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionResolver extends DefaultHandlerExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus
	private <T> RestResult<T> runtimeExceptionHandler(Exception e) {
		LOGGER.error("runtimeExceptionHandler:", e);
		return RestResultGenerator.error(ErrorCodeEnum.SERVER_ERROR);
	}

	/**
     * 处理拦截未知的运行时异常
     * @param e
     * @return
     */
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ApiError> badRequestException(RuntimeException e) {
		LOGGER.error(e.getMessage());
        ApiError apiError = new ApiError(BAD_REQUEST.value(),e.getMessage());
        return buildResponseEntity(apiError);
	}
	
    /**
     * 处理所有接口数据验证异常
     * @param e
     * @returns
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
    	LOGGER.error("illegalParamsExceptionHandler:"+e.getMessage());
        String[] str = e.getBindingResult().getAllErrors().get(0).getCodes()[1].split("\\.");
        StringBuffer msg = new StringBuffer(str[1]+":");
        msg.append(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        ApiError apiError = new ApiError(BAD_REQUEST.value(),msg.toString());
        return buildResponseEntity(apiError);
    }
	
	 /**
     * 统一返回
     * @param apiError
     * @return
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<ApiError>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

}
