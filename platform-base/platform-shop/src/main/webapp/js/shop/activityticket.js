$(function () {
    $("#jqGrid").Grid({
        url: '../activityticket/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '用户id', name: 'memberId', index: 'member_id', width: 80, hidden: true},
			{label: '分类', name: 'ticketKind', index: 'ticket_kind', width: 80, hidden: true},
			{label: '活动编号', name: 'itemNo', index: 'item_no', width: 80, hidden: true},
			{label: '名称', name: 'ticketName', index: 'ticket_name', width: 80},
			{label: '图片', name: 'ticketLogo', index: 'ticket_logo', width: 80},
			{label: '价格', name: 'ticketPrice', index: 'ticket_price', width: 80},
			{label: '原价格', name: 'ticketOrgPrice', index: 'ticket_org_price', width: 80},
			{label: '总量', name: 'maxSum', index: 'max_sum', width: 80},
			{label: '已售', name: 'sellSum', index: 'sell_sum', width: 80},
			{label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, formatter: function (value) {
                return transDate(value);
            }},
			]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		activityTicket: {},
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
			vm.activityTicket = {};
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
            let url = vm.activityTicket.id == null ? "../activityticket/save" : "../activityticket/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.activityTicket),
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
				    url: "../activityticket/delete",
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
                url: "../activityticket/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.activityTicket = r.activityTicket;
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