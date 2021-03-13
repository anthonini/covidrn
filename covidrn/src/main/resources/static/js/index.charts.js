var CovidRN = CovidRN || {};

CovidRN.CasosConfirmadosCovid = (function() {
	
	function CasosConfirmadosCovid() {
		this.ctx = $('#totalCasosConfirmadosCovidCanvas')[0].getContext('2d');
	}
	
	CasosConfirmadosCovid.prototype.iniciar = function() {
		$.ajax({
			url: 'dashboard/totalCasosConfirmados',
			method: 'GET',
			success: onReceivedData.bind(this)
		});
	}
	
	function onReceivedData(dadosDiarios) {
		var dias = [];
		var valores = [];
		dadosDiarios.forEach(function(dadoDiario){
			dias.push(dadoDiario.data);
			valores.push(dadoDiario.total);
		});
		
		
		var monthSaleChart = new Chart(this.ctx, {
		    type: 'line',
		    data: {
		    	labels: dias,
		    	datasets: [{
		    		label: 'Casos Confirmados',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                pointBorderColor: "rgba(26,179,148,1)",
	                data: valores,
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