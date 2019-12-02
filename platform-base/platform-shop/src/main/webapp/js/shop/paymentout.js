$(function () {
    $("#jqGrid").Grid({
        url: '../paymentout/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '交易单号', name: 'outTradeNo', index: 'out_trade_no', width: 80,align : "center"},
			{label: '用户id', name: 'userId', index: 'user_id', width: 80, hidden: true,align : "center"},
			{label: '会员账号', name: 'userName', index: 'user_name', width: 80,align : "center"},
			
			{label: '申请日期', name: 'createTime', index: 'create_time', width: 80,align : "center", formatter: function (value) {
                return transDate(value);
            }},
            
//			{label: '收款名称', name: 'receiptAccountRealName', index: 'receipt_account_real_name', width: 80,align : "center"},
            
			{label: '提币地址', name: 'receiptAccount', index: 'receipt_account', width: 80,align : "center"},
			
			{label: '提现数量', name: 'amount', index: 'amount', width: 80,align : "center", formatter: function (value, col, row) {
				var toPayMoney=0;
				toPayMoney=row.amount+row.fee;
				return toPayMoney;
			}},
			{label: '手续费', name: 'fee', index: 'fee', width: 80,align : "center"},
			{label: '实际到账', name: 'amount', index: 'amount', width: 80,align : "center"},
			{label: '提币状态', name: 'status', index: 'status', width: 80,align : "center", formatter: function (value) {
				if(value==1){
					return "待处理";
				}else if(value==2){
					return "已处理";
				}else if(value==3){
					return "已取消";
				}else{
					return "-";
				}
                return value;
            }},
            {label: '处理时间', name: 'paymentDate', index: 'payment_date', width: 80,align : "center", formatter: function (value) {
                return transDate(value);
            }},
            {label: '操作', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
            	var operationStr="";
            	if(row.status==2||row.status==3){
            		return "已操作";
            	}
            	 operationStr=operationStr+'<button class="btn btn-outline btn-danger btn-sm" onclick="vm.showUserStat(\'' + row.userId +'\')">查看对账</button><br/>';
            	 operationStr=operationStr+ '<button class="btn btn-outline btn-info" onclick="vm.toAudit(\'' + row.id +'\',\'' + row.outTradeNo +'\',\'' + row.status +'\',\'' + row.userId +'\',\''+ row.userName +'\',\''+row.receiptAccountRealName+'\',\''+row.receiptAccount+'\',\''+row.amount+'\')"><i class="fa fa-info-circle"></i>&nbsp;审核</button>';
            	return operationStr;
            }}
		],
		height:"100%",
        rowNum:50,
        jsonReader : { 
            root: "page.list",//返回的数组集合  
            page: "page.currPage",//当前页数
            total: "page.totalPage", //总页数 
            records: "page.totalCount",//总行数  
            repeatitems: false,//指明每行的数据是可以重复的，如果设为false，则会从返回的数据中按名字来搜索元素  
            userdata: "queryStat", //我们需要用的一些并不想显示到页面上的数据 
        },
//	    jsonReader: {
//            root: "page.list",
//            page: "page.currPage",
//            total: "page.totalPage",
//            records: "page.totalCount",
//            userdata: "queryStat",
//        },
	    gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
            vm.toQueryStat();
        }
    });
    vm.getStatuss();
   
});
let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		paymentOut: {
			id:'',
			outTradeNo:'',
			status:'',
			userId:'',
			userName:'',
			receiptAccountRealName:'',
			receiptAccount:'',
			amount:''
		},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			status: '',
			outTradeNo:'',
			userName:'',
			queryDate:[]
		},
		statuss:[],
		queryStat:{}
	},
	methods: {
		toQueryStat:function(){
			var queryStat=$("#jqGrid").jqGrid('getGridParam', 'userData');
			console.log("--------queryStat---------",queryStat);
			vm.queryStat=queryStat;
		},
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.paymentOut = {};
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
            let url = vm.paymentOut.id == null ? "../paymentout/save" : "../paymentout/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.paymentOut),
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
				    url: "../paymentout/delete",
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
                url: "../paymentout/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.paymentOut = r.paymentOut;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
			var confirmTimeStart='';
		    var confirmTimeEnd='';
		    if(vm.q.queryDate&&vm.q.queryDate.length==2&&vm.q.queryDate[0]&&vm.q.queryDate[1]){
		    	confirmTimeStart=vm.q.queryDate[0].dateFormat('yyyy-MM-dd 00:00:00');
		    	confirmTimeEnd=vm.q.queryDate[1].dateFormat('yyyy-MM-dd 23:59:59');
		    }
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
                	'status': vm.q.status,
                	'outTradeNo':vm.q.outTradeNo,
                	'userName':vm.q.userName,
                	'confirmTimeStart':confirmTimeStart,
                	'confirmTimeEnd':confirmTimeEnd
                	},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
        			status: '',
        			outTradeNo:'',
        			userName:'',
        			queryDate:[]
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
        },
        toAudit: function(id,outTradeNo,status,userId,userName,receiptAccountRealName,receiptAccount,amount){
        	vm.paymentOut={
    			id:'',
    			outTradeNo:'',
    			status:'',
    			userId:'',
    			userName:'',
    			receiptAccountRealName:'',
    			receiptAccount:'',
    			amount:'',
    			method:5
    		};
        	vm.paymentOut.id=id;
        	vm.paymentOut.outTradeNo=outTradeNo;
        	vm.paymentOut.userId=userId;
        	vm.paymentOut.userName=userName;
        	vm.paymentOut.receiptAccountRealName=receiptAccountRealName;
        	vm.paymentOut.receiptAccount=receiptAccount;
        	vm.paymentOut.amount=amount;
        	vm.paymentOut.method=5;
        	console.log(vm.paymentOut)
        	openWindow({
                title: "审核打款",
                area: ['420px', '370px'],
                content: jQuery("#payMentOutLayer"),
                btn: ['确认打款', '取消打款'],
                btn1: function (index) {
                	
                	vm.toPaymentOut();
                    layer.close(index);
                    
                },
                btn2: function (index) {
                	confirm('确定要取消打款？', function () {
                		vm.toPaymentBack();
                    	layer.close(index);
        			});
                },
                
            });
        },
        
        toPaymentOut:function(){
        	Ajax.request({
			    url: '../paymentout/toPaymentOut',
                params: JSON.stringify(vm.paymentOut),
                type: "POST",
                async: false,
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        },
        
        toPaymentBack:function(){
        	Ajax.request({
			    url: '../paymentout/toPaymentBack',
                params: {
                	id:vm.paymentOut.id,
                 },
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        },
        
        /**
         * 状态
         */
        getStatuss: function () {
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=paymentoutStatus",
                async: false,
                successCallback: function (r) {
                    vm.statuss = r.list;
                }
            });
        },
        showUserStat: function (userId) {
            openWindow({
            	top:true,
                type: 2,
                area: ['95%', '95%'],
                title: '<i class="fa fa-print"></i>会员对账',
                content: 'shop/userstat.html?userId=' + userId,
                btn: ['确定'],
            })
        },
        toConfirmPaymentOut: function (event) {
            let ids = getSelectedRows("#jqGrid");
			if (ids == null){
				return;
			}
			confirm('确定处理选中的记录？', function () {
                Ajax.request({
				    url: "../paymentout/toConfirmPaymentOut",
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
        exportPaymentOut: function () {
        	var confirmTimeStart='';
		    var confirmTimeEnd='';
        	 if(vm.q.queryDate&&vm.q.queryDate.length==2&&vm.q.queryDate[0]&&vm.q.queryDate[1]){
 		    	confirmTimeStart=vm.q.queryDate[0].dateFormat('yyyy-MM-dd 00:00:00');
 		    	confirmTimeEnd=vm.q.queryDate[1].dateFormat('yyyy-MM-dd 23:59:59');
 		    }
           var postData={
                 	'status': vm.q.status,
                 	'outTradeNo':vm.q.outTradeNo,
                 	'userName':vm.q.userName,
                 	'confirmTimeStart':confirmTimeStart,
                 	'confirmTimeEnd':confirmTimeEnd
                 	};
        	exportFile('#rrapp', '../paymentout/export',postData);
        },
	}
});