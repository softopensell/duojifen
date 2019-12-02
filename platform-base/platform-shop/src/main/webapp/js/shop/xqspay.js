let vm = new Vue({
	el: '#rrapp',
	data: {
		payPassword: '',
	},
	methods: {
        modPassWord: function(){
            confirm('确定要修改平台支付密码？', function () {
            	  Ajax.request({
     				 url: "../user/updatePlatPayPassword",
                     params:{"payPassword":vm.payPassword},
                     type: "POST",
                     successCallback: function () {
                         alert('操作成功', function (index) {
                             vm.reload();
                         });
     				}
     			});
			});
        },
	},
	created: function () {
        let that = this;
    }
	
});