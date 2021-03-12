var Brewer = Brewer || {};

Brewer.MonthSaleChart = (function() {
	
	function MonthSaleChart() {
		this.ctx = $('#monthSaleChart')[0].getContext('2d');
	}
	
	MonthSaleChart.prototype.iniciar = function() {
		$.ajax({
			url: 'covidrn/totalByMonth',
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
	                pointRadius: 1,
		    	}]
		    },
		});
	}
	
	return MonthSaleChart;
	
}());

$(function() {
	var monthSaleChart = new Brewer.MonthSaleChart();
	monthSaleChart.iniciar();
});