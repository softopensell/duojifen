$(function () {
	/**
	 * 积分转出列表
	 */
    $("#jqGrid").Grid({
        url: '../paymentinfo/list',
        postData: {'queryType':'integralcount'},
        multiselect:false,
        userDataOnFooter:true,
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '用户名', name: 'userName', index: 'user_name', width: 120},
			{label: '手机号码', name: 'userMobile', index: 'userMobile', width: 150},
			{label: '转出时间', name: 'paymentDate', index: 'payment_date', width: 120, formatter: function (value) {
                return transDate(value);
            }},
			{label: '转出数量', name: 'amount', index: 'amount', width: 120}
			]
    });
     vm.getInitTotal();
});


let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		initTotal: {allTotal:'',nowTotal:'',nowDate:''},
		/**
		 * q,查询条件
		 */
		q: {
			userName:'',
			userMobile:'',
			queryDate:[]
		}
	},
	methods: {
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
                	'userName':vm.q.userName,
                	'userMobile':vm.q.userMobile,
                	'confirmTimeStart':confirmTimeStart,
                	'confirmTimeEnd':confirmTimeEnd
                	},
                page: page
            }).trigger("reloadGrid");
		},
        reloadSearch: function() {
            vm.q= {
        			userName:'',
        			userMobile:'',
        			queryDate:[]
        		};
            vm.reload();
        },
        /**
         * 合计统计
         */
        getInitTotal: function () {
            Ajax.request({
                url: "../paymentinfo/queryIntegralCountInitData",
                async: false,
                successCallback: function (r) {
                	vm.initTotal=r.result;
                }
            });
        },
	}
});