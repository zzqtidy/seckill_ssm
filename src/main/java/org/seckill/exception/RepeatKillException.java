package org.seckill.exception;

/**
 * 重复秒杀异常(运行期异常，Spring的事务只能回滚运行期的异常，
 * 如果不是运行期的异常，Spring的事务是无法帮我们回滚的）
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
