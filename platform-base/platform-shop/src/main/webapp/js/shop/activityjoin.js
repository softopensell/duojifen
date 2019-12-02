$(function () {
    $("#jqGrid").Grid({
        url: '../activityjoin/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '活动itemNo', name: 'itemNo', index: 'item_no', width: 80},
			{label: '状态', name: 'statu', index: 'statu', width: 80},
			{label: '类别', name: 'joinType', index: 'join_type', width: 80},
			{label: '参加', name: 'joinInviteNumber', index: 'join_invite_number', width: 80},
			{label: '', name: 'joinInviteName', index: 'join_invite_name', width: 80},
			{label: '', name: 'joinInviteLogo', index: 'join_invite_logo', width: 80},
			{label: '', name: 'joinInviteTitle', index: 'join_invite_title', width: 80},
			{label: '', name: 'joinInviteContenttype', index: 'join_invite_contentType', width: 80},
			{label: '', name: 'joinInviteDesc', index: 'join_invite_desc', width: 80},
			{label: '', name: 'joinInviteImage', index: 'join_invite_image', width: 80},
			{label: '', name: 'joinInviteWriteName', index: 'join_invite_write_name', width: 80},
			{label: '', name: 'joinInviteWriteDate', index: 'join_invite_write_date', width: 80},
			{label: '', name: 'joinInviteReadStatu', index: 'join_invite_read_statu', width: 80},
			{label: '', name: 'joinMemberId', index: 'join_member_id', width: 80},
			{label: '', name: 'joinAuditStatu', index: 'join_audit_statu', width: 80},
			{label: '', name: 'joinAuditRefuse', index: 'join_audit_refuse', width: 80},
			{label: '', name: 'createTime', index: 'create_time', width: 80},
			{label: '', name: 'updateTime', index: 'update_time', width: 80},
			]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		activityJoin: {},
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
			vm.activityJoin = {};
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
            let url = vm.activityJoin.id == null ? "../activityjoin/save" : "../activityjoin/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.activityJoin),
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
				    url: "../activityjoin/delete",
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
                url: "../activityjoin/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.activityJoin = r.activityJoin;
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