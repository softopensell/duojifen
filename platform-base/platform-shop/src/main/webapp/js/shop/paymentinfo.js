$(function () {
    $("#jqGrid").Grid({
        url: '../paymentinfo/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '流水号', name: 'sn', index: 'sn', width: 80},
			{label: '交易日期', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
                return transDate(value);
            }},
            {label: '会员账号', name: 'userName', index: 'user_name', width: 80},
			{label: '收益类型', name: 'memo', index: 'memo', width: 80},
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
		paymentInfo: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			sn: '',
		    userName: '',
		    moneyTypeWallet: '',
		},
		curUser:{},
		money:'',
		payType:-1,
		remark:'',
		payPassword:'',
		tapAction:0,
		
	},
	methods: {
		selectUser: function () {
            openWindow({
                top:true,
                type: 2,
                area: ['95%', '95%'],
                title: '选择用户',
                content: ['shop/user.html'],
                btn: ['确定', '取消'],
                btn1: function (index,layero) {
                	var artFrameWd=layero.find("iframe")[0].contentWindow;
                    var grid = artFrameWd.$("#jqGrid");
                    var rowKey = grid.getGridParam("selrow");
                    if (!rowKey) {
                        iview.Message.error("请选择一条记录");
                        return;
                    }
                    var selectedIDs = grid.getGridParam("selarrrow");
                    if (selectedIDs.length > 1) {
                        iview.Message.error("只能选择一条记录");
                        return;
                    }
                    var id=selectedIDs[0];
                    console.log("---memberId---", id);
                    var userInfo=grid.jqGrid('getRowData', id);
                    console.log("---userInfo---", userInfo);
                    vm.curUser=userInfo;
                    top.layer.close(index);
                    layer.close(index);
              }
            });
        },
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.paymentInfo = {};
		},
		addTakeOffMoneyTap: function () {
			vm.showList = false;
			vm.title = "平台扣费";
			vm.curUser={};
			vm.money=0;
			vm.remark='';
			vm.tapAction=1;
		},
		toAddMoneyTap: function () {
			vm.showList = false;
			vm.title = "平台充值";
			vm.curUser={};
			vm.money=0;
			vm.remark='';
			vm.tapAction=2;
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
		toTakeOffMoney: function (event) {
		    var userId=vm.curUser.userId;
	       	var money=vm.money;
	       	if(vm.tapAction==1){
	       		Ajax.request({
				    url: '../user/toTakeOffMoney',
	               params: {
	            	userId:userId,
	            	payType:vm.payType,
	               	money:money,
	               	remark:vm.remark,
	                },
	               successCallback: function (r) {
	            	    vm.curUser={};
		       			vm.money=0;
		       			vm.remark='';
		       			vm.tapAction=0;
	                   alert('操作成功', function (index) {
	                       vm.reload();
	                   });
	               }
	       		 });	
	       	}else if(vm.tapAction==2){
	       		Ajax.request({
				   url: '../user/toAddMoney',
	               params: {
	            	userId:userId,
	            	payType:vm.payType,
	               	money:money,
	               	remark:vm.remark,
	               	payPassword:vm.payPassword,
	                },
	               successCallback: function (r) {
	            	    vm.curUser={};
		       			vm.money=0;
		       			vm.remark='';
		       			vm.tapAction=0;
	                   alert('操作成功', function (index) {
	                       vm.reload();
	                   });
	               }
	       		 });	
	       	}
	       	
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
                	'sn': vm.q.sn,
                	'userName': vm.q.userName,
                	'moneyTypeWallet': vm.q.moneyTypeWallet
                	},
                page: page
            }).trigger("reloadGrid");
		},
        reloadSearch: function() {
            vm.q = {
                sn: '',
                userName:'',
                moneyTypeWallet:''	
            }
            vm.reload();
        },
	}
});