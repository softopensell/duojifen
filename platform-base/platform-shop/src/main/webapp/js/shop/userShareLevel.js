$(function () {
    $("#jqGrid").Grid({
        url: '../userblack/list?blackType=2',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '会员ID', name: 'userId', index: 'user_id', width: 80},
			{label: '会员名称', name: 'userName', index: 'user_name', width: 80},
			{label: '分级', name: 'userShareLevel', index: 'user_share_level', width: 80},
			{
                label: '时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
                    return transDate(value);
                }
            },
            ]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		userBlack: {
			userId:'',
			userName:'',
			blackType:0,
		},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.userBlack = {
					userId:'',
					userName:'',
					blackType:2,
			};
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
            let url = vm.userBlack.id == null ? "../userblack/save" : "../userblack/update";
            vm.userBlack.blackType=2
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.userBlack),
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
				    url: "../userblack/delete",
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
                url: "../userblack/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.userBlack = r.userBlack;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
                	'userShareLevel':0,
                	'name': vm.q.name,
                	},
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
        selectAllUser: function () {
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
                    var userInfo=grid.jqGrid('getRowData', id);
                    console.log("---userInfo---", userInfo);
                    vm.userBlack.userId=userInfo.userId;
                    vm.userBlack.userName=userInfo.userName;
                    top.layer.close(index);
                    layer.close(index);
              }
            });
        },
	}
});