var CovidRN = CovidRN || {};

CovidRN.CasosConfirmadosCovid = (function() {
	
	function CasosConfirmadosCovid() {
		this.ctxDiario = $('#casosConfirmadosCovidPorDiaCanvas')[0].getContext('2d');
		this.ctxTotal = $('#totalCasosConfirmadosCovidCanvas')[0].getContext('2d');
	}
	
	CasosConfirmadosCovid.prototype.iniciar = function() {
		$.ajax({
			url: 'dashboard/totalCasosConfirmados',
			method: 'GET',
			success: onReceivedData.bind(this)
		});
	}
	
	function onReceivedData(dadosDiarios) {
		var dados = {
			datas : [],
			totalDia : [],
			total : []
		}

		dadosDiarios.forEach(function(dadoDiario){
			dados.datas.push(dadoDiario.data);
			dados.totalDia.push(dadoDiario.dia);
			dados.total.push(dadoDiario.total);
		});
		
		criarChartCasosConfirmadosPorDia.call(this, dados);
		criarChartCasosConfirmadosTotal.call(this, dados);
	}
	
	function criarChartCasosConfirmadosPorDia(dados) {
		console.log('dados', dados)
		var bar = document.getElementById("casosConfirmadosCovidPorDiaCanvas").getContext('2d');
        var theme_g1 = bar.createLinearGradient(0, 0, 500, 0);
        theme_g1.addColorStop(0, 'rgba(29, 233, 182, 0.4)');
        theme_g1.addColorStop(1, 'rgba(29, 196, 233, 0.5)');
        
		var casosConfirmadosPorDiaChart = new Chart(this.ctxDiario, {
		    type: 'line',
		    data: {
		    	labels: dados.datas,
		    	datasets: [{
		    		label: 'Casos Confirmados por Dia',
		    		backgroundColor: theme_g1,
	                pointBorderColor: theme_g1,
	                data: dados.totalDia,
	                pointRadius: 0,
					lineTension: 0,
					borderWidth: 2
		    	}]
		    },
		    options: {
				responsive: true,
				tooltips: {
					mode: 'index',
					intersect: false,
				},
				hover: {
					mode: 'nearest',
					intersect: true
				},
			}
		});
	}
	
	function criarChartCasosConfirmadosTotal(dados) {
		var bar = document.getElementById("casosConfirmadosCovidPorDiaCanvas").getContext('2d');
        var theme_g1 = bar.createLinearGradient(0, 0, 500, 0);
        theme_g1.addColorStop(0, 'rgba(29, 233, 182, 0.4)');
        theme_g1.addColorStop(1, 'rgba(29, 196, 233, 0.5)');
        
		var casosConfirmadosTotalChart = new Chart(this.ctxTotal, {
		    type: 'line',
		    data: {
		    	labels: dados.datas,
		    	datasets: [{
		    		label: 'Casos Confirmados',
		    		backgroundColor: theme_g1,
	                pointBorderColor: theme_g1,
	                data: dados.total,
	                pointRadius: 0,
					lineTension: 0,
					borderWidth: 2
		    	}]
		    },
		    options: {
				responsive: true,
				tooltips: {
					mode: 'index',
					intersect: false,
				},
				hover: {
					mode: 'nearest',
					intersect: true
				},
			}
		});
	}
	
	return CasosConfirmadosCovid;
	
}());

$(function() {
	var casosConfirmadosCovid = new CovidRN.CasosConfirmadosCovid();
	casosConfirmadosCovid.iniciar();
});