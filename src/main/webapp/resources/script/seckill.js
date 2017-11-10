//使用模块化
var seckill = {
    URL: {},
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        }
        else
            return false;
    },
    detail: {
        init: function (params) {
            var killPhone = $.cookie("killPhone");
            var seckillId = params["seckillId"];
            var startTime = params["startTime"];
            var endTime = params["endTime"];
            console.log(seckillId+" "+seckill.validatePhone(killPhone));
            if (!seckill.validatePhone(killPhone)) {
                //绑定Phone 控制输出
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',//禁止点击其他区域退出弹出层
                    keyboard: false//禁止键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    console.log("进入手机号点击");
                    var inputPhone = $('#killPhoneKey').val();
                    console.log(inputPhone);
                    if(seckill.validatePhone(inputPhone)){
                        //电话号码写入cookie
                        //cookie默认7天过期， 同时只存放于/seckill下
                        $.cookie('killPhone',inputPhone,{expire:7,path:'/seckill'});
                        window.location.reload();
                    }
                    else
                    {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                })
            }
        }
    }
}