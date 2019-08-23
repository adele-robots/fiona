var tags = {
		"!top": ["aiml"],
		aiml: {
			attrs: {
				version			: ["1.0.1"],				
				xmlns			: ["http://alicebot.org/2001/AIML-1.0.1"],
				"xmlns:html"	: ["http://www.w3.org/1999/xhtml"],
				"xmlns:xsi"		: ["http://www.w3.org/2001/XMLSchema-instance"],
				"xsi:schemaLocation"	: ["http://alicebot.org/2001/AIML-1.0.1 http://aitools.org/aiml/schema/AIML.xsd"]
			},
			children: ["category"]
		},
		category: {
			attrs: {},
			children: ["pattern", "that", "template"]
		},
		pattern: {
			attrs: {},
			children: []
		},
		that: {
			attrs: {},
			children: []
		},
		template: {
			attrs: {},
			children: ["srai", "random", "think", "get"]
		},
		srai: {
			attrs: {},
			children: []
		},
		random: {
			attrs: {},
			children: ["li"]
		},
		li: {
			attrs: {},
			children: []
		},
		think: {
			attrs: {},
			children: ["set"]
		},
		set: {
			attrs: {
				name	: []
			},
			children: []
		},
		get: {
			attrs: {
				name	: []
			},
			children: []
		}
};