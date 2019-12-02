$(function () {
    $("#jqGrid").Grid({
        url: '../activitytickeorder/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '订单号', name: 'orderNo', index: 'order_no',align : "center", width: 80},
			{label: '活动编号', name: 'itemNo', index: 'item_no', align : "center",width: 80, hidden: true},
			{label: '参加类型', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
				if(parseInt(row.orderType)==0){
              		 return '报名';
              	 }else if(parseInt(row.orderType)==1){
              		 return '会议';
              	 }else if(parseInt(row.orderType)==100){
              		 return '门票';
              	 }
              	 return '-'; 
              	} 
            },
            {label: '支付方式', width: 80, align: 'memberStatu', sortable: false,align : "center", formatter: function (value, col, row) {
            	if(parseInt(row.memberStatu)==0){
            		return '余额';
            	}else if(parseInt(row.memberStatu)==1){
            		return '积分';
            	}else if(parseInt(row.memberStatu)==2){
            		return '资产';
            	}
            	return '-'; 
             } 
            },
            {label: '账号', name: 'ticketName', index: 'ticket_name',align : "center", width: 80},
			{label: '用户id', name: 'memberId', index: 'member_id',align : "center", width: 80, hidden: true},
			{label: '姓名', name: 'memberName', index: 'member_name',align : "center", width: 80},
			{label: '电话', name: 'memberPhone', index: 'member_phone',align : "center", width: 80},
			{label: '门票', name: 'ticketId', index: 'ticket_id', align : "center",width: 80, hidden: true},
			{label: '内容', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
				if(parseInt(row.orderType)==0){
              		 return "地址："+row.memberAddress+",备注："+row.memberRemark;
              	 }else if(parseInt(row.orderType)==1){
              		 return "团队："+row.memberAddress+",备注："+row.memberRemark;
              	 }else if(parseInt(row.orderType)==100){
              		 return '门票类型：'+row.ticketName+",单价："+row.ticketPrice+",备注："+row.memberRemark;
              	 }
              	 return '-'; 
              	} 
            },
			{label: '数量', name: 'ticketSum', index: 'ticket_sum', align : "center",width: 80},
			{label: '总价 ', name: 'totalPrice', index: 'total_price', align : "center",width: 80},
			{label: '状态', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
				if(parseInt(row.orderStatu)==0){
              		 return '<span class="badge badge-danger">未支付</span>';
              	 }else if(parseInt(row.orderStatu)==1){
              		 return '<span class="badge badge-danger">已支付，待审核</span>';
              	 }else if(parseInt(row.orderStatu)==2){
              		 return '<span class="badge badge-danger">已支付，发货</span>';
              	 }else if(parseInt(row.orderStatu)==21){
              		 return '<span class="badge badge-danger">已支付，退款</span>';
              	 }else if(parseInt(row.orderStatu)==100){
              		 return '<span class="badge badge-danger">签到</span>';
              	 }else if(parseInt(row.orderStatu)==4){
              		 return '<span class="badge badge-danger">取消</span>';
              	 }
              	 return '-'; 
              	} 
            },
			{label: '更新时间', name: 'updateTime', index: 'update_time', align : "center",width: 80, formatter: function (value) {
                return transDate(value);
            }},
            
//			{label: '操作', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
//            	var operationStr="";
//            	 operationStr=operationStr+ '<button class="btn btn-outline btn-info" onclick="vm.toAudit(\'' + row.id +'\')"><i class="fa fa-info-circle"></i>&nbsp;审核</button>';
//            	return operationStr;
//            }},
            
			]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		activityTickeorder: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			memberPhone: '',
			memberName: '',
            itemNo: '',
		},
		activityItems:[]
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.activityTickeorder = {};
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
            let url = vm.activityTickeorder.id == null ? "../activitytickeorder/save" : "../activitytickeorder/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.activityTickeorder),
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
				    url: "../activitytickeorder/delete",
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
                url: "../activitytickeorder/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.activityTickeorder = r.activityTickeorder;
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
            vm.q = {
            	memberPhone: '',
            	memberName: '',
                itemNo: '',
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
        toAudit:function(){
        	
        },
        modActivityItem:function(){
        	
        },
        toConfrimTickOrder:function(){
        	let ids = getSelectedRows("#jqGrid");
			if (ids == null){
				return;
			}
			confirm('确定报名？', function () {
                Ajax.request({
				    url: "../activitytickeorder/toConfrimTickOrder",
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
        toRefundTickOrder:function(){
        	let ids = getSelectedRows("#jqGrid");
			if (ids == null){
				return;
			}
			confirm('确定取消并退款？', function () {
                Ajax.request({
				    url: "../activitytickeorder/toRefundTickOrder",
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
        toCheckInOrder:function(){
        	
        	let ids = getSelectedRows("#jqGrid");
        	if (ids == null){
        		return;
        	}
        	confirm('确定签到？', function () {
        		Ajax.request({
        			url: "../activitytickeorder/toCheckInOrder",
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
        
        toDownload:function(){
            exportFile('#rrapp', '../activitytickeorder/toDownload',vm.q);
        },
        getAllActivityItem: function(){
            Ajax.request({
                url: "../activityitem/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.activityItems = r.list;
                }
            });
		},
	},
	created: function () {
        let that = this;
        that.getAllActivityItem();
    }
});