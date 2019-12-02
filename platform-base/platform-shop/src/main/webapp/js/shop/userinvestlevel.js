$(function () {
    $("#jqGrid").Grid({
        url: '../userinvestlevel/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '级别ID', name: 'userLevelType', index: 'user_level_type', width: 80},
			{label: '名称', name: 'userLevelName', index: 'user_level_name', width: 80},
//			{label: '消费额度', name: 'userLevelMoneyValue', index: 'user_level_money_value', width: 80},
//			{label: '节点奖励层级', name: 'userLevelNodeLevel', index: 'user_level_node_level', width: 80},
//			{label: '关联商品', name: 'goodsSn', index: 'goods_sn', width: 80},
			{label: '最小消费额度', name: 'userLevelConsumedMin', index: 'user_level_consumed_min', width: 80},
			{label: '最大消费额度', name: 'userLevelConsumedMax', index: 'user_level_consumed_max', width: 80},
			{label: '奖励倍数', name: 'userLevelTime', index: 'user_level_time', width: 80},
			
			{
                label: '状态', name: 'statu', index: 'statu', width: 80, formatter: function (value,col,row) {
                	if(value==0){
                		return "有效"
                	}else if(value==1){
                		return "删除"
                	}
                }
            },
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
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
		userInvestLevel: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.userInvestLevel = {};
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
            let url = vm.userInvestLevel.id == null ? "../userinvestlevel/save" : "../userinvestlevel/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.userInvestLevel),
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
				    url: "../userinvestlevel/delete",
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
                url: "../userinvestlevel/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.userInvestLevel = r.userInvestLevel;
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
        }
	}
});