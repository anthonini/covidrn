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
		
		var bar = document.getElementById("totalCasosConfirmadosCovidCanvas").getContext('2d');
        var theme_g1 = bar.createLinearGradient(0, 0, 500, 0);
        theme_g1.addColorStop(0, 'rgba(29, 233, 182, 0.4)');
        theme_g1.addColorStop(1, 'rgba(29, 196, 233, 0.5)');
        
		var monthSaleChart = new Chart(this.ctx, {
		    type: 'line',
		    data: {
		    	labels: dias,
		    	datasets: [{
		    		label: 'Casos Confirmados',
		    		backgroundColor: theme_g1,
	                pointBorderColor: theme_g1,
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