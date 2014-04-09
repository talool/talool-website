$(function() {
	setTimeout(function() {
		var step = ${chartStep};
		var size = ${chartSize};
		var horizons = ${data};
		var id = "#${componentMarkupId}";
		window.oo.cubismChart(id,horizons,step,size);
	}, 100 );
});


/*

//On mousemove, reposition the chart values to match the rule.
			//context.on("focus", function(i) {
			//  d3.selectAll(".value").style("right", i == null ? null : context.size() + 20 - i + "px");
			//});
			
// TODO review the greens in cubism and replace with teals
			var colors = ["rgba(220,220,220,1)",
		    	"rgba(25, 188, 185, 0.7)",
		    	"rgba(241, 90, 36, 0.7)",
		    	"rgba(80, 185, 72, 0.7)",
		    	"rgba(237, 28, 36, 0.6)"];

d3.select("#example1").call(function(div) {

  div.append("div")
      .attr("class", "axis")
      .call(context.axis().orient("top"));

  div.selectAll(".horizon")
      .data([foo, bar, foo.add(bar), foo.subtract(bar)])
    .enter().append("div")
      .attr("class", "horizon")
      .call(context.horizon().extent([-20, 20]));

  div.append("div")
      .attr("class", "rule")
      .call(context.rule());

});

d3.select("#example2a").call(function(div) {
  div.datum(foo);

  div.append("div")
      .attr("class", "horizon")
      .call(context.horizon()
        .height(120)
        .mode("mirror")
        .colors(["#bdd7e7","#bae4b3"])
        .title("Area (120px)")
        .extent([-10, 10]));

  div.append("div")
      .attr("class", "horizon")
      .call(context.horizon()
        .height(30)
        .mode("mirror")
        .colors(["#bdd7e7","#bae4b3"])
        .title("Area (30px)")
        .extent([-10, 10]));
});

d3.select("#example2b").call(function(div) {
  div.datum(foo);

  div.append("div")
      .attr("class", "horizon")
      .call(context.horizon()
        .height(120)
        .colors(["#bdd7e7","#bae4b3"])
        .title("Horizon, 1-band (120px)")
        .extent([-10, 10]));

  div.append("div")
      .attr("class", "horizon")
      .call(context.horizon()
        .height(60)
        .colors(["#6baed6","#bdd7e7","#bae4b3","#74c476"])
        .title("Horizon, 2-band (60px)")
        .extent([-10, 10]));

  div.append("div")
      .attr("class", "horizon")
      .call(context.horizon()
        .height(40)
        .colors(["#3182bd","#6baed6","#bdd7e7","#bae4b3","#74c476","#31a354"])
        .title("Horizon, 3-band (40px)")
        .extent([-10, 10]));

  div.append("div")
      .attr("class", "horizon")
      .call(context.horizon()
        .height(30)
        .colors(["#08519c","#3182bd","#6baed6","#bdd7e7","#bae4b3","#74c476","#31a354","#006d2c"])
        .title("Horizon, 4-band (30px)")
        .extent([-10, 10]));

});

// On mousemove, reposition the chart values to match the rule.
context.on("focus", function(i) {
  d3.selectAll(".value").style("right", i == null ? null : context.size() - i + "px");
});


*/