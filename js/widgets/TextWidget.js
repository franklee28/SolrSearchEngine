(function ($) {
	AjaxSolr.TextWidget = AjaxSolr.AbstractTextWidget.extend({
		init: function () {
			var self = this;
			$(this.target).find('input').click(function() {
				var value = $(textarea).val();
				var words = value.split("\n");
				var query = "\"" + words[0] + "\"";
				for (i = 1;i < words.length; i++) {
					query += "OR";
					query += "\"";
					query += words[i];
					query += "\"";
				}
				if (query && self.set(query)) {
					self.doRequest();
				}
			});
		},

		afterRequest: function () {
			//$(this.target).find('input').val('');
		}
	});
})(jQuery);
