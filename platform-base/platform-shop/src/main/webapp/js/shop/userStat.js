$(function () {
	 let userId = getQueryString("userId");
	 let moneyTypeWallet = getQueryString("moneyTypeWallet");
	 console.log("------userId-------",userId);
    $("#jqGrid").Grid({
        url: '../paymentinfo/list',
        postData: {
        	userId:userId,
        	moneyTypeWallet:moneyTypeWallet
        },
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '流水号', name: 'sn', index: 'sn', width: 80},
			{label: '交易日期', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
                return transDate(value);
            }},
            {label: '会员账号', name: 'userName', index: 'user_name', width: 80},
			{label: '奖金数量', name: 'amount', index: 'amount', width: 80},
			{label: '订单类型', name: 'orderType', index: 'order_type', width: 80},
			{label: '订单号', name: 'orderNo', index: 'order_no', width: 80},
			{label: '支付类型', name: 'paymentType', index: 'payment_type',sortable: false,formatter: function (value) {
                if(value=="0"){
                	return "IN";
                }else if(value=="1"){
                	return "OUT";
                }
            }},
            {label: '状态', name: 'status', index: 'status',sortable: false,formatter: function (value) {
                if(value=="0"){
                	return "待支付";
                }else if(value=="1"){
                	return "支付中";
                }else if(value=="2"){
                	return "待发货";
                }else if(value=="8"){
                	return "已支付";
                }else if(value=="9"){
                	return "已支付";
                }else if(value=="10"){
                	return "已完成";
                }else if(value=="-1"){
                	return "已取消";
                }else if(value=="11"){
                	return "已失败";
                }else if(value=="14"){
                	return "已退款";
                }
                
            }},
            {label: '备注', name: 'memo', index: 'memo', width: 80},
		],
		height:"100%",
        rowNum:50,
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
		userTotalStat:{},
        showList: true,
        title: null,
		paymentInfo: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			userId:'',
		    name: '',
		    userName: '',
		    moneyTypeWallet: '',
		},
		statBalance:0
	},
	filters: {
		  numFilter (value) {
		    let realVal = parseFloat(value).toFixed(4)
		    return realVal
		  }
	 },
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.paymentInfo = {};
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
            let url = vm.paymentInfo.id == null ? "../paymentinfo/save" : "../paymentinfo/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.paymentInfo),
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
				    url: "../paymentinfo/delete",
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
                url: "../paymentinfo/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.paymentInfo = r.paymentInfo;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
                	'userId': vm.q.userId,
                	'sn': vm.q.name,
                	'userName': vm.q.userName,
                	'moneyTypeWallet': vm.q.moneyTypeWallet
                	},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
            	userId: vm.q.userId,
                name: '',
                userName:'',
                moneyTypeWallet:''	
            }
            vm.reload();
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
        },
        
        /**
         * 统计账号金额
         */
        getUserTotalStat: function () {
        	let that=this;
            Ajax.request({
                url: "../platformstat/toStatUserMoney",
                params: {
                	userId:that.q.userId
                },
                async: false,
                successCallback: function (r) {
                	that.userTotalStat=r.result;
                	var tempBalance=0;
                	var totalStat=r.result;
                	if(totalStat.userEntity.userPreBalance){
                		tempBalance=tempBalance+totalStat.userEntity.userPreBalance;
                	}
                	if(totalStat.payTotalMap.walletType10000){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType10000;
                	}
                	
                	if(totalStat.payTotalMap.walletType1020){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType1020;
                	}
                	
                	if(totalStat.payTotalMap.walletType6){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType6;
                	}
                	
                	if(totalStat.payTotalMap.walletType3){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType3;
                	}
                	if(totalStat.payTotalMap.walletType43){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType43;
                	}
                	if(totalStat.payTotalMap.walletType411){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType411;
                	}
                	if(totalStat.payTotalMap.walletType421){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType421;
                	}
                	if(totalStat.payTotalMap.walletType431){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType431;
                	}
                	if(totalStat.payTotalMap.walletType64){
                		tempBalance=tempBalance+totalStat.payTotalMap.walletType64;
                	}
                	
                	if(totalStat.payTotalMap.walletType62){
                		tempBalance=tempBalance-totalStat.payTotalMap.walletType62;
                	}
                	if(totalStat.payTotalMap.walletType5){
                		tempBalance=tempBalance-totalStat.payTotalMap.walletType5;
                	}
                	if(totalStat.payTotalMap.walletType2){
                		tempBalance=tempBalance-totalStat.payTotalMap.walletType2;
                	}
                	if(totalStat.payTotalMap.walletType0){
                		tempBalance=tempBalance-totalStat.payTotalMap.walletType0;
                	}
                	if(totalStat.payTotalMap.walletType1){
                		tempBalance=tempBalance-totalStat.payTotalMap.walletType1;
                	}
                	if(totalStat.payTotalMap.walletType10001){
                		tempBalance=tempBalance-totalStat.payTotalMap.walletType10001;
                	}
                	that.statBalance=tempBalance;
                }
            });
        },
	},
	
	created:function (params) {
		 let userId = getQueryString("userId");
		 let moneyTypeWallet = getQueryString("moneyTypeWallet");
		 this.q.userId=userId;
		 this.q.moneyTypeWallet=moneyTypeWallet;
     	 this.getUserTotalStat();
     }
});