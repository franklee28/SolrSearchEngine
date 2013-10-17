var map;

(function ($) {
AjaxSolr.GoogleMapWidget = AjaxSolr.AbstractWidget.extend({

	afterRequest: function () {
		var self = this;
		//Start a separate request to get the locations of all results and pinpoint to the google map
		var callback = function(response) {
			self.initialize();
			var heatmapData = [];
			for (var i = 0, l = response.response.docs.length; i < l; i++) {
				var doc = response.response.docs[i];
				if (typeof(doc.loc_p) != "undefined") {
					var coords = doc.loc_p.split(",");
					var latLng = new google.maps.LatLng(coords[0],coords[1]);
					var marker = new google.maps.Marker({
						position: latLng,
						map: map
					});
					//heatmapData.push(latLng);
				}
			}
			/*var heatmap = new google.maps.visualization.HeatmapLayer({
				data: heatmapData,
				dissipating: false,
				map: map
			});*/
		}
		
		var num = this.manager.response.response.numFound;
		var params = [];
		params.push('q=' + this.manager.store.get('q').val());
		params.push('fl=id,loc_p');
		params.push('rows=' + num);
		$.getJSON(Manager.solrUrl + 'select?' + params.join('&') + '&wt=json&json.wrf=?', {}, callback);
	},
	
	initialize: function() {
		var mapOptions = {
			center: new google.maps.LatLng(42, -94),
			zoom: 4,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	}
});
})(jQuery);
