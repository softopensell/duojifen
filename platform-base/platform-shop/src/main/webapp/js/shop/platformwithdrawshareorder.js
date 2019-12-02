$(function () {
    $("#jqGrid").Grid({
        url: '../platformwithdrawshareorder/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '序号', name: 'orderDateNo', index: 'order_date_no', width: 80},
			{label: '本次奖励总额', name: 'totalPayMoney', index: 'total_pay_money', width: 80},
			{label: '时间', name: 'shareStartDate', index: 'share_start_date', width: 120,align : "center", formatter: function (value) {
                return transDate(value);
			}},
			{label: '运营分成', name: 'shareUserDaySys', index: 'share_user_day_sys', width: 80},
			{label: '一星', name: 'shareUserDayV1', index: 'share_user_day_v1', width: 80},
			{label: '二星', name: 'shareUserDayV2', index: 'share_user_day_v2', width: 80},
			{label: '三星', name: 'shareUserDayV3', index: 'share_user_day_v3', width: 80},
			{label: '四星', name: 'shareUserDayV4', index: 'share_user_day_v4', width: 80},
			{label: '五星', name: 'shareUserDayV5', index: 'share_user_day_v5', width: 80},
			{label: '一钻', name: 'shareUserDayV6', index: 'share_user_day_v6', width: 80},
			{label: '二钻', name: 'shareUserDayV7', index: 'share_user_day_v7', width: 80},
			{label: '三钻', name: 'shareUserDayV8', index: 'share_user_day_v8', width: 80},
			{label: '四钻', name: 'shareUserDayV9', index: 'share_user_day_v9', width: 80},
			{label: '五钻', name: 'shareUserDayV10', index: 'share_user_day_v10', width: 80},
			{label: '更新时间', name: 'updateTime', index: 'update_time', width: 120,align : "center", formatter: function (value) {
                return transDate(value);
			}},
			{label: '操作', width: 180, align: 'center', sortable: false, formatter: function (value, col, row) {
	            	var operationStr="--";
	              operationStr='<button class="btn btn-outline btn-info" onclick="vm.showUserStat(' + row.userId + ')"><i class="fa fa-info-circle"></i>&nbsp;明细</button>';
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
		platformWithdrawShareOrder: {},
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
			vm.platformWithdrawShareOrder = {};
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
            let url = vm.platformWithdrawShareOrder.id == null ? "../platformwithdrawshareorder/save" : "../platformwithdrawshareorder/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.platformWithdrawShareOrder),
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
				    url: "../platformwithdrawshareorder/delete",
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
                url: "../platformwithdrawshareorder/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.platformWithdrawShareOrder = r.platformWithdrawShareOrder;
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