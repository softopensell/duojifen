$(function () {
    $("#jqGrid").Grid({
        url: '../platformwithdrawshare/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '会员ID', name: 'userId', index: 'user_id', width: 80},
			{label: '会员', name: 'userName', index: 'user_name', width: 80},
			{
                label: '类别', name: 'withdrawType', index: 'withdraw_type', width: 80,align : "center", formatter: function (value,col,row) {
                	if(value==1){
                		return "共识机制"
                	}else if(value==0){
                		return "共识股东"
                	}else{
                		return "-";
                	}
                }
            },
            {
            	label: '星级类型', name: 'withdrawTypeStar', index: 'withdraw_type_star', width: 80,align : "center", formatter: function (value,col,row) {
            		if(value==1){
            			return "一星"
            		}else if(value==2){
            			return "二星"
            		}else if(value==3){
            			return "三星"
            		}else if(value==4){
            			return "四星"
            		}else if(value==5){
            			return "五星"
            		}else if(value==6){
            			return "一钻"
            		}else if(value==7){
            			return "二钻"
            		}else if(value==8){
            			return "三钻"
            		}else if(value==9){
            			return "四钻"
            		}else if(value==10){
            			return "五钻"
            		}else{
            			return "-";
            		}
            	}
            },
			{label: '更新时间', name: 'updateTime', index: 'update_time', width: 120,align : "center", formatter: function (value) {
                return transDate(value);
			}},
	         {label: '操作', width: 180, align: 'center', sortable: false, formatter: function (value, col, row) {
	            	var operationStr="--";
	              operationStr='<button class="btn btn-outline btn-info" onclick="vm.showUserStat(' + row.userId + ')"><i class="fa fa-info-circle"></i>&nbsp;历史记录</button>';
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
		platformWithdrawShare: {},
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
			vm.platformWithdrawShare = {};
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
            let url = vm.platformWithdrawShare.id == null ? "../platformwithdrawshare/save" : "../platformwithdrawshare/update";
            vm.platformWithdrawShare.userName=vm.curUser.userName;
            vm.platformWithdrawShare.userId=vm.curUser.userId;
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.platformWithdrawShare),
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
				    url: "../platformwithdrawshare/delete",
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
                url: "../platformwithdrawshare/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.platformWithdrawShare = r.platformWithdrawShare;
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
                content: 'shop/userstat.html?userId=' + userId+"&moneyTypeWallet=1020",
                btn: ['确定'],
            })
        },
	}
});