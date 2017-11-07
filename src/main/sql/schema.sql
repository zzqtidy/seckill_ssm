--数据库的初始化脚本
-- 创建数据库
CREATE  database seckill;
--使用数据库
use seckill;
--创建秒杀库存表
CREATE TABLE seckill(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
name varchar(120) NOT NULL COMMENT '商品名称',
number INT NOT NULL COMMENT '库存数量',
start_time datetime NOT NULL COMMENT '秒杀开启时间',
end_time datetime NOT NULL COMMENT '秒杀结束时间',
create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_starttime(start_time),
KEY idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';
--初始化数据
insert into
  seckill(name,number,start_time,end_time)
VALUES
('1000元秒杀IPHONE6',100,'2017-11-11 00:00:00','2017-11-12 00:00:00'),
('100元秒杀红米2',100,'2017-11-11 00:00:00','2017-11-12 00:00:00'),
('99元秒杀魅蓝2',100,'2017-11-11 00:00:00','2017-11-12 00:00:00'),
('1500元秒杀IPHONE6S',100,'2017-11-11 00:00:00','2017-11-12 00:00:00');


--秒杀成功明细表
--用户登录认证相关信息
create TABLE success_killed(
seckill_id bigint NOT NULL COMMENT '秒杀商品ID',
user_phone BIGINT NOT NULL COMMENT '用户手机号',
statue tinyint NOT NULL COMMENT '状态标识：-1：无效 0：成功 1：已经付款 2：已经发货',
create_time datetime DEFAULT now()  COMMENT '创建时间',
PRIMARY KEY (seckill_id,user_phone),
KEY idx_create_time(create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';