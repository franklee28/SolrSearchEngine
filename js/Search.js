var Manager;

(function ($) {

	$(function () {
		Manager = new AjaxSolr.Manager({
			solrUrl: 'http://192.168.1.102:8080/solr-example/'
		});
		Manager.addWidget(new AjaxSolr.ResultWidget({
			id: 'result',
			target: '#docs'
		}));
		Manager.addWidget(new AjaxSolr.PagerWidget({
			id: 'pager',
			target: '#pager',
			prevLabel: '&lt;',
			nextLabel: '&gt;',
			innerWindow: 1,
			renderHeader: function (perPage, offset, total) {
				$('#pager-header').html($('<span style="padding-left:10px;"></span>').text(Math.min(total, offset + 1) + ' to ' + Math.min(total, offset + perPage) + ' of ' + total));
			}
		}));
		Manager.addWidget(new AjaxSolr.TextWidget({
			id: 'text',
			target: '#search'
		}));
		Manager.addWidget(new AjaxSolr.GoogleMapWidget({
			id: 'map',
			target: '#map-canvas'
		}));
		Manager.init();
		//Manager.store.addByValue('q', '*:*');
		//Manager.doRequest();
	});
})(jQuery);