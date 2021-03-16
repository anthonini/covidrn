var CovidRN = CovidRN || {};

CovidRN.Visualizar = (function(){
	function Visualizar() {
		this.cores = ['#1DE9B6','#1DC4E9','#04A9F5','#899FD4','#A389D4','#AEFFD8','#8AFFC1','#8FDC97','#00C4F5','#00DADD','#2EEBB4','#4E94ED','#767CDB','#0075BC','#F4C22B','#67B7DC','#FDDA08','#1DDCC8','#CCE5FF','#E2E3E5','#D4EDDA','#F8D7DA','#F55D52','#C7F9CC','#9396D4'];
		this.coresCopia = this.cores.slice();
		this.donutChartCtx = $('#donutChart')[0].getContext('2d');
		this.barChartCtx = $('#barChart')[0].getContext('2d');
		this.horizontalChart = $('#horizontalChart')[0].getContext('2d');
	}
	
	Visualizar.prototype.iniciar = function() {
		shuffleArray(this.coresCopia);
		$.ajax({
			url: '/comparar-periodos/periodos',
			method: 'GET',
			success: onReceivedData.bind(this)
		});
	}
	
	function onReceivedData(periodos) {
		var labels = []; 
		var data = [];
		var backgroundColor = [];
		var total = 0;
		
		periodos.forEach(function(periodo) {
			labels.push(periodo.inicio + ' a ' + periodo.fim);
			data.push(periodo.total);
			backgroundColor.push(getCor.call(this));
			total += periodo.total;
		}.bind(this));
	
		var donutChartData = {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: backgroundColor,
            }]
        };
        var donutChart = new Chart(this.donutChartCtx, {
            type: 'doughnut',
            data: donutChartData,
            responsive: true,
            options: {
				responsive: true,
				legend: {
					position: 'top',
				},
				title: {
					display: true,
					text: 'Casos Confirmados'
				},
				animation: {
					animateScale: true,
					animateRotate: true
				},
				maintainAspectRatio: false,
			}
        });
        
		var barChart = new Chart(this.barChartCtx, {
		    type: 'bar',
		    data: {
		        labels: labels,
		        datasets: [{
		            label: 'Casos Confirmados',
		            data: data,
		            backgroundColor: backgroundColor[0],
		            borderWidth: 1
		        }]
		    },
		    options: {
		        scales: {
		            yAxes: [{
		                ticks: {
		                    beginAtZero: true
		                }
		            }]
		        }
		    }
		});
	
		var horizontalBar = new Chart(this.horizontalChart, {
				type: 'horizontalBar',
				data: {
					labels: labels,
					datasets: [{
						label: 'Casos Confirmados',
						backgroundColor: backgroundColor[1],
						borderWidth: 1,
						data: data
					}],
				},options: {
					elements: {
						rectangle: {
							borderWidth: 2,
						}
					},
					responsive: false,
					legend: {
						position: 'top',
					},
					title: {
						display: true,
						text: 'Casos Confirmados'
					},
					 scales: {
				        xAxes: [{
				            ticks: {
				                beginAtZero: true
				            }
				        }]
				    }
				}
			});
		
	}
	
	function getCor() {
		if(this.coresCopia.length === 0) {
			this.coresCopia = this.cores.slice();
			shuffleArray(this.coresCopia);
		}
		
		return this.coresCopia.shift();
	}
	
	function shuffleArray(array) {
		for(let i = array.length-1; i > 0; i--){
		  const j = Math.floor(Math.random() * i)
		  const temp = array[i]
		  array[i] = array[j]
		  array[j] = temp
		}
	}

	return Visualizar;
}());

$(function() {
	var visualizar = new CovidRN.Visualizar();
	visualizar.iniciar();
});