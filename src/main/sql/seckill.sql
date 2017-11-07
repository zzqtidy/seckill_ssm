--row_count()函数返回上一次修改行数

CREATE PROCEDURE `seckill`.`execute_seckill`
(
in v_seckill_id bigint,
in v_phone bigint,
in v_kill_time TIMESTAMP ,
out r_result INT
)
BEGIN
  DECLARE insert_count int DEFAULT 0;
  start TRANSACTION;
  INSERT ignore into success_killed (seckill_id,user_phone,create_time) VALUES (v_seckill_id,v_phone,v_kill_time);
  select ROW_COUNT () into insert_count;
  if (insert_count < 0) THEN
    ROLLBACK ;
    set r_result = -2;
  elseif (insert_count = 0) THEN
    ROLLBACK ;
    set r_result = -1;
  ELSE
    UPDATE seckill
    set NUMBER = NUMBER -1
    WHERE NUMBER >1
    and seckill_id = v_seckill_id
    and end_time > v_kill_time
    and start_time < v_kill_time ;

    select ROW_COUNT () into insert_count;
    if insert_count < 0 THEN
      ROLLBACK ;
      set r_result = -2;
    elseif insert_count = 0 THEN
      ROLLBACK ;
      set r_result = -1;
    ELSE
      set r_result = 1;
      COMMIT ;
    end if;
  end if;
end;
$$
