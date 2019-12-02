$(function () {
    $("#jqGrid").Grid({
        url: '../platformfwmanager/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '会员ID', name: 'fwUserId', index: 'fw_user_id', width: 80},
			{label: '会员名称', name: 'fwUserName', index: 'fw_user_name', width: 80},
			{label: '服务中心', name: 'fwName', index: 'fw_name', width: 80},
			{label: '当前业绩', name: 'fwCurYj', index: 'fw_cur_yj', width: 80},
			{label: '上次结算时间', name: 'fwCurDate', index: 'fw_cur_date', width: 120,align : "center", formatter: function (value) {
                return transDate(value);
			}},
			{label: '结算次数', name: 'fwTotalResetTime', index: 'fw_total_reset_time', width: 80},
			{label: '累计业绩', name: 'fwTotalYj', index: 'fw_total_yj', width: 80},
			{label: '累计奖励', name: 'fwTotalPayMoney', index: 'fw_total_pay_money', width: 80},
			
			{label: '更新时间', name: 'updateTime', index: 'update_time', width: 120,align : "center", formatter: function (value) {
	                return transDate(value);
	         }},
	         {label: '操作', width: 180, align: 'center', sortable: false, formatter: function (value, col, row) {
	            	var operationStr="--";
	              operationStr='<button class="btn btn-outline btn-info" onclick="vm.toOpenJsModal(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;结算</button>';
	              operationStr=operationStr+'<button class="btn btn-outline btn-info" onclick="vm.showUserStat(' + row.fwUserId + ')"><i class="fa fa-info-circle"></i>&nbsp;历史记录</button>';
	              return operationStr;
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
		platformFwManager: {
			
		},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		},
		curUser:{},
		 toJssModal:false,
	     money:0,
		
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
                    //设置管理作品
//                    vm.artFwFrameItem.frameProductNumber = products.pnumber;
//                    vm.artFwFrameItem.frameProductType = 0;
                    //设置新的框Id
//                    vm.user.createUserId = id;
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
			vm.platformFwManager = {};
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
            let url = vm.platformFwManager.id == null ? "../platformfwmanager/save" : "../platformfwmanager/update";
            vm.platformFwManager.fwUserName=vm.curUser.userName;
            vm.platformFwManager.fwUserId=vm.curUser.userId;
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.platformFwManager),
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
				    url: "../platformfwmanager/delete",
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
                url: "../platformfwmanager/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.platformFwManager = r.platformFwManager;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
                name: ''
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
        showUserStat: function (userId) {
            openWindow({
            	top:true,
                type: 2,
                area: ['95%', '95%'],
                title: '<i class="fa fa-print"></i>会员对账',
                content: 'shop/userstat.html?userId=' + userId+"&moneyTypeWallet=1010",
                btn: ['确定'],
            })
        },
        toOpenJsModal:function(id){
        	vm.money=0;
        	vm.toJssModal=true;
        	this.getInfo(id);
        },
        toJsMoneyTap:function(){
        	var money=vm.money;
        	Ajax.request({
			    url: '../platformfwmanager/toPayFwManagerShare',
                params: {
                	id:vm.platformFwManager.id,
                	toPay:money,
                 },
			    contentType: "application/json",
                successCallback: function (r) {
                	vm.toJssModal=false;
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        },
	}
});