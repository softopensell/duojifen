$(function () {
    $("#jqGrid").Grid({
        url: '../platformstat/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '编号', name: 'statDateNumber', index: 'stat_date_number', width: 100, align: 'center'},
			{
                label: '会员总数', name: 'statUserDayAddV0Sum', index: 'stat_user_day_add_sum', width: 100,align : "center", formatter: function (value,col,row) {
                	
                	return ""+row.statUserDayAddSum+"/"+row.statUserTotalSum;
                }
            },
            {
            	label: '未激活', name: 'statUserDayAddV0Sum', index: 'stat_user_day_add_v0_sum', width: 100,align : "center", formatter: function (value,col,row) {
            		
            		return ""+row.statUserDayAddV0Sum+"/"+row.statUserV0Sum;
            	}
            },
            {
            	label: '新增V1', name: 'statUserDayAddV1Sum', index: 'stat_user_day_add_v1_sum', width: 100,align : "center", formatter: function (value,col,row) {
            		
            		return ""+row.statUserDayAddV1Sum+"/"+row.statUserV1Sum;
            	}
            },
            {
            	label: '新增V2', name: 'statUserDayAddV2Sum', index: 'stat_user_day_add_v2_sum', width: 100,align : "center", formatter: function (value,col,row) {
            		
            		return ""+row.statUserDayAddV2Sum+"/"+row.statUserV2Sum;
            	}
            },
            {
            	label: '新增V3', name: 'statUserDayAddV3Sum', index: 'stat_user_day_add_v3_sum', width: 100,align : "center", formatter: function (value,col,row) {
            		
            		return ""+row.statUserDayAddV3Sum+"/"+row.statUserV3Sum;
            	}
            },
            {
            	label: '新增V4', name: 'statUserDayAddV4Sum', index: 'stat_user_day_add_v4_sum', width: 100,align : "center", formatter: function (value,col,row) {
            		
            		return ""+row.statUserDayAddV4Sum+"/"+row.statUserV4Sum;
            	}
            },
			
			{label: '累计资产', name: 'statUserTotalCz', index: 'stat_user_total_cz', width: 100, align: 'center'},
			{label: '余额总量', name: 'statUserTotalZc', index: 'stat_user_total_zc', width: 100, align: 'center'},
			{label: '总积分', name: 'statUserTotalJf', index: 'stat_user_total_jf', width: 100, align: 'center'},
			{label: '剩余资产', name: 'statUserTotalSyZc', index: 'stat_user_total_sy_zc', width: 100, align: 'center'},
			{label: '总基金', name: 'statUserTotalFund', index: 'stat_user_total_fund', width: 100, align: 'center'},
			
			{label: '今日分成比例', name: 'statDayRate', index: 'stat_day_rate', width: 100, align: 'center'},
			{label: '今日资产收益', name: 'statDayMoneyQy', index: 'stat_day_money_qy', width: 100, align: 'center'},
			{label: '今日分享收益', name: 'statDayMoneyFw', index: 'stat_day_money_fw', width: 100, align: 'center'},
			{label: '今日社区收益', name: 'statDayMoneySq', index: 'stat_day_money_sq', width: 100, align: 'center'},
			{label: '今日星星收益', name: 'statDayMoneyXx', index: 'stat_day_money_xx', width: 100, align: 'center'},
			{label: '今日基金收益', name: 'statDayMoneyFund', index: 'stat_day_money_fund', width: 100, align: 'center'},
			
			{label: '今日余额支付数量', name: 'statDayPayBalanceSum', index: 'stat_day_pay_balance_sum', width: 100, align: 'center'},
			{label: '今日余额支付总额', name: 'statDayPayBalance', index: 'stat_day_pay_balance', width: 100, align: 'center'},
			{label: '今日积分支付数量', name: 'statDayPayJfSum', index: 'stat_day_pay_jf_sum', width: 100, align: 'center'},
			{label: '今日积分支付总额', name: 'statDayPayJf', index: 'stat_day_pay_jf', width: 100, align: 'center'},
			
			{label: '今日积分兑换总额', name: 'statDayJfDh', index: 'stat_day_pay_jf_dh', width: 100, align: 'center'},
			
			{label: '余额转出数量', name: 'statDayBalanceZzOutSum', index: 'stat_day_balance_zz_out_sum', width: 100, align: 'center'},
			{label: '余额转出总额', name: 'statDayBalanceZzOut', index: 'stat_day_balance_zz_out', width: 100, align: 'center'},
			
			{label: '余额转入数量', name: 'statDayBalanceZzInSum', index: 'stat_day_balance_zz_in_sum', width: 100, align: 'center'},
			{label: '余额转入总额', name: 'statDayBalanceZzIn', index: 'stat_day_balance_zz_in', width: 100, align: 'center'},
			
			{label: '充值数量', name: 'statDayMoneyRechargeSum', index: 'stat_day_money_recharge_sum', width: 100, align: 'center'},
			{label: '充值金额', name: 'statDayMoneyRecharge', index: 'stat_day_money_recharge', width: 100, align: 'center'},
			
			{label: '提现数量', name: 'statDayMoneyTxSum', index: 'stat_day_money_tx_sum', width: 100, align: 'center'},
			{label: '提现金额', name: 'statDayMoneyTx', index: 'stat_day_money_tx', width: 100, align: 'center'},
			
			],
			height:"100%",
            rowNum:50,
			shrinkToFit: false,
			autoScroll: true,          //shrinkToFit: false,autoScroll: true,这两个属性产生水平滚动条   
	        autowidth: true,          //必须要,否则没有水平滚动条
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		platformStat: {},
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
			vm.platformStat = {};
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
            let url = vm.platformStat.id == null ? "../platformstat/save" : "../platformstat/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.platformStat),
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
				    url: "../platformstat/delete",
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
                url: "../platformstat/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.platformStat = r.platformStat;
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
        exportStat:function(){
           var postData={};
        	exportFile('#rrapp', '../platformstat/exportStat',postData);
        },
	}
});