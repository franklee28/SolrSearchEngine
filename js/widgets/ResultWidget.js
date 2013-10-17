(function ($) {
AjaxSolr.ResultWidget = AjaxSolr.AbstractWidget.extend({
	start: 0,
	
	beforeRequest: function () {
		$(this.target).html($('<img>').attr('src', 'images/ajax-loader.gif'));
	},
  
	afterRequest: function () {
		$(this.target).empty();
		for (var i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
			var doc = this.manager.response.response.docs[i];
			$(this.target).append(this.template(doc));
		}
	},

	template: function (doc) {
		var snippet = '';
		if (typeof(doc.loc_p) == "undefined") {
			snippet += 'Location: No Location Found For This File';
		}
		else {
			snippet += 'Location: ' + doc.loc_p;
		}

		var output = '<div><h2>' + doc.id + '</h2>';
		//output += '<p id="links_' + doc.id + '" class="links"></p>';
		output += '<p>' + snippet + '</p></div>';
		output += '</div>';
		return output;
	}
});
})(jQuery);
