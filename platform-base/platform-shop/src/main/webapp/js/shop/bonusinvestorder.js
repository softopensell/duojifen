$(function () {
    $("#jqGrid").Grid({
        url: '../bonusinvestorder/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '流水号', name: 'investOrderNo', index: 'invest_order_no', width: 80},
			{label: '会员', name: 'userName', index: 'user_id', width: 80},
			{label: '奖励订单号', name: 'consumedOrderNo', index: 'consumed_order_no', width: 80},
			{label: '购买额度', name: 'buyMoney', index: 'buy_money', width: 80},
			{
                label: '支付类型', name: 'payType', index: 'pay_type', width: 80, formatter: function (value,col,row) {
                	if(value==0){
                		return "货到付款"
                	}else if(value==1){
                		return "微信支付"
                	}else if(value==2){
                		return "支付宝支付"
                	}else if(value==3){
                		return "余额支付"
                	}else if(value==4){
                		return "积分支付"
                	}else if(value==5){
                		return "现金支付"
                	}else if(value==6){
                		return "刷卡支付"
                	}
                }
            },
			{
                label: '支付状态', name: 'payStatus', index: 'pay_status', width: 80, formatter: function (value,col,row) {
                	if(value==0){
                		return "待支付"
                	}else if(value==1){
                		return "付款中"
                	}else if(value==2){
                		return "付款成功"
                	}else if(value==3){
                		return "付款失败"
                	}else if(value==4){
                		return "退款中"
                	}else if(value==5){
                		return "退款成功"
                	}else if(value==6){
                		return "退款失败"
                	}
                }
            },
			{
                label: '状态', name: 'statu', index: 'statu', width: 80, formatter: function (value,col,row) {
                	if(value==0){
                		return "有效"
                	}else if(value==1){
                		return "失效"
                	}
                }
            },
			{label: '时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
                return transDate(value);
             }}
			],
			height:"100%",
            rowNum:50,
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		bonusInvestOrder: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			investOrderNo: '',
			userId: '',
			consumedOrderNo: '',
			payStatus: '',
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.bonusInvestOrder = {};
		},
		update: function (event) {
            let id = getSelectedRow("#jqGrid");
			if (id == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
            let url = vm.bonusInvestOrder.id == null ? "../bonusinvestorder/save" : "../bonusinvestorder/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.bonusInvestOrder),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
		},
		del: function (event) {
            let ids = getSelectedRows("#jqGrid");
			if (ids == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../bonusinvestorder/delete",
                    params: JSON.stringify(ids),
                    type: "POST",
				    contentType: "application/json",
                    successCallback: function () {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
					}
				});
			});
		},
		getInfo: function(id){
            Ajax.request({
                url: "../bonusinvestorder/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.bonusInvestOrder = r.bonusInvestOrder;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: vm.q,
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q ={
    			investOrderNo: '',
    			userId: '',
    			consumedOrderNo: '',
    			payStatus: '',
    		 }
            vm.reload();
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        }
	}
});