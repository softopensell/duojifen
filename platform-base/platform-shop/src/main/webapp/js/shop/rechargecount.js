$(function () {
	/**
	 * 积分转出列表
	 */
    $("#jqGrid").Grid({
        url: '../paymentinfo/rechargelist',
        postData: {'queryType':'integralcount',"moneyTypeWallet":6},
        multiselect:false,
        userDataOnFooter:true,
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '用户名', name: 'userName', index: 'user_name', width: 80},
			{label: '手机号码', name: 'userMobile', index: 'userMobile', width: 80},
			{label: '充值金额', name: 'amount', index: 'amount', width: 80},
			{label: '类型', name: 'status', index: 'status', width: 80,sortable: false,formatter: function (value) {
                if(value=="0"){
                	return "申请中";
                }else if(value=="8"){
                	return "成功";
                }else if(value=="10"){
                	return "成功";
                }else if(value=="11"){
                	return "取消";
                }
            }},
            {label: '创建时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
                return transDate(value);
            }},
			{label: '充值时间', name: 'paymentDate', index: 'payment_date', width: 80, formatter: function (value) {
                return transDate(value);
            }},
            {label: '备注', name: 'orderDesc', index: 'order_desc', width: 80},
            {
                label: '其他', name: 'memo', index: 'memo', width: 80, formatter: function (value, col, row) {
                   if(61==row.moneyTypeWallet){
                	   return transImg(value,true);
                   }else if(6==row.moneyTypeWallet){
                	   return transImg(value,true);
                   }
                   return value;
                }
             },
             {
            	 label: '服务中心', name: 'logisticsName', index: 'logistics_name', width: 80, formatter: function (value, col, row) {
            		 if(row.logisticsName){
            			 return value;
            		 }else {
            			 return ""
            		 }
            	 }
             },
            {label: '操作', width: 120, align: 'center', sortable: false, formatter: function (value, col, row) {
            	var operationStr="--";
            	if(row.status=="0"){
            		operationStr='<button class="btn btn-outline btn-info" onclick="vm.modRechargeMoney(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;修改金额</button>';
            		operationStr=operationStr+'<button class="btn btn-outline btn-info" onclick="vm.agreeRechargeMoney(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;确认充值</button>';
            		operationStr=operationStr+'<button class="btn btn-outline btn-info" onclick="vm.refuseRechargeMoney(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;取消充值</button>';
                }
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
            gridComplete: function () {
                //隐藏grid底部滚动条
                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
                vm.toQueryStat();
            }
    });
     vm.getInitTotal();
});


let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		initTotal: {allTotal:'',nowTotal:'',nowDate:''},
		paymentInfo:{},
		/**
		 * q,查询条件
		 */
		q: {
			moneyTypeWallet:6,
			userName:'',
			userMobile:'',
			status:'',
			queryDate:[]
		},
		 rechargeMoneyModal:false,
	     money:0,
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
                	'queryType':'integralcount',
                	'moneyTypeWallet':vm.q.moneyTypeWallet,
                	'userName':vm.q.userName,
                	'userMobile':vm.q.userMobile,
                	'status':vm.q.status,
                	'confirmTimeStart':confirmTimeStart,
                	'confirmTimeEnd':confirmTimeEnd
                	},
                page: page
            }).trigger("reloadGrid");
		},
        reloadSearch: function() {
            vm.q= {
            		moneyTypeWallet:6,
        			userName:'',
        			userMobile:'',
        			status:'',
        			queryDate:[]
        		};
            vm.reload();
        },
        /**
         * 合计统计
         */
        getInitTotal: function () {
            Ajax.request({
                url: "../paymentinfo/queryRechargeInitData",
                async: false,
                successCallback: function (r) {
                	vm.initTotal=r.result;
                }
            });
        },
        getInfo: function(id){
            Ajax.request({
                url: "../paymentinfo/info/"+id,
                async: false,
                successCallback: function (r) {
                    vm.paymentInfo = r.paymentInfo;
                    vm.money=r.paymentInfo.amount;
                }
            });
		},
        
        modRechargeMoney: function (id) {
        	vm.money=0;
        	vm.rechargeMoneyModal=true;
        	vm.getInfo(id);
        },
        
        toRechargeMoneyTap:function(){
        	var id=vm.paymentInfo.id;
        	var money=vm.money;
        	var modAmontParams={
        			id:id,
                	amount:money,
        	};
        	Ajax.request({
			    url: '../paymentinfo/modAmount',
                params: {
                	id:id,
                	amount:money,
                 },
//                params:JSON.stringify(modAmontParams),
			    contentType: "application/json",
                successCallback: function (r) {
                	vm.rechargeMoneyModal=false;
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        },
        
        agreeRechargeMoney: function (id) {
        	confirm('确定充值？', function () {
        		Ajax.request({
        			url: "../paymentinfo/agreeRecharge/"+id,
        			params: {},
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
        
        
        refuseRechargeMoney: function (id,money) {
        	confirm('取消充值？', function () {
                Ajax.request({
                	url: "../paymentinfo/refuseRecharge/"+id,
            		async: false,
            		successCallback: function (r) {
            			alert('操作成功', function (index) {
                            vm.reload();
                        });
            		}
            	});
			});
        },
        exportRecharge:function(){
        	var confirmTimeStart='';
 		    var confirmTimeEnd='';
 		    if(vm.q.queryDate&&vm.q.queryDate.length==2&&vm.q.queryDate[0]&&vm.q.queryDate[1]){
 		    	confirmTimeStart=vm.q.queryDate[0].dateFormat('yyyy-MM-dd 00:00:00');
 		    	confirmTimeEnd=vm.q.queryDate[1].dateFormat('yyyy-MM-dd 23:59:59');
 		    }
           var postData={
        		   'queryType':'integralcount',
                	'moneyTypeWallet':vm.q.moneyTypeWallet,
                	'userName':vm.q.userName,
                	'userMobile':vm.q.userMobile,
                	'status':vm.q.status,
                	'confirmTimeStart':confirmTimeStart,
                	'confirmTimeEnd':confirmTimeEnd
                 	};
        	exportFile('#rrapp', '../paymentinfo/exportRecharge',postData);
        	
        },
        
        
        
	}
});