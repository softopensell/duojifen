$(function () {
	 let monitorDateNumber = getQueryString("monitorDateNumber");
    $("#jqGrid").Grid({
        url: '../platformmonitorstadetail/list',
        postData: {'monitorDateNumber': monitorDateNumber},
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '序号', name: 'monitorDateNumber', index: 'monitor_date_number', width: 80},
			{label: '用户ID', name: 'monitorMemberId', index: 'monitor_member_id', width: 80,align : "center"},
			{label: '用户', name: 'monitorMemberName', index: 'monitor_member_name', width: 80,align : "center"},
			{label: '监控类型', name: 'monitorType', index: 'monitor_type', width: 80,align : "center"},
			{label: '异常说明', name: 'monitorContent', index: 'monitor_content', width: 80,align : "center"},
			{label: '时间', name: 'createTime', index: 'create_time', width: 120,align : "center", formatter: function (value) {
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
		platformMonitorStadetail: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: '',
		    monitorDateNumber: '',
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.platformMonitorStadetail = {};
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
            let url = vm.platformMonitorStadetail.id == null ? "../platformmonitorstadetail/save" : "../platformmonitorstadetail/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.platformMonitorStadetail),
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
				    url: "../platformmonitorstadetail/delete",
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
                url: "../platformmonitorstadetail/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.platformMonitorStadetail = r.platformMonitorStadetail;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
                	'name': vm.q.name,
                	'monitorDateNumber': vm.q.monitorDateNumber
                	},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
                name: '',
                monitorDateNumber: vm.q.monitorDateNumber
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
	},
	created: function () {
        let that = this;
        let monitorDateNumber = getQueryString("monitorDateNumber");
        that.q.monitorDateNumber=monitorDateNumber;
    }
});