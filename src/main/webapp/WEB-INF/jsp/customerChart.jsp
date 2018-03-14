<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
  <head>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">

      // Load the Visualization API and the corechart package.
      google.charts.load('current', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.charts.setOnLoadCallback(drawCityStatistic);
      google.charts.setOnLoadCallback(drawGenderStatistic);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawCityStatistic() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'City');
        data.addColumn('number', 'count');
        
        <c:forEach items="${cityStatistic}" var="cityStatistic">
        	data.addRow(['${cityStatistic.key}', ${cityStatistic.value}]);
  		</c:forEach>
        
        //This doesn't work, at the server side the javascript variable 'cityStatistic' has not been defined, it is void.
        /*
        var cityStatistic = new Object();
        <c:forEach items="${customers}" var="customers">
          if('${customers.city}' in cityStatistic){
        	  cityStatistic['${customers.city}']=1;
          }else{
        	  cityStatistic['${customers.city}']=cityStatistic['${customers.city}']+1;
          }
    	</c:forEach>
    	
        <c:forEach items="${cityStatistic}" var="cityStatistic">
          data.addRow(['${cityStatistic.key}', ${cityStatistic.value}]);
    	</c:forEach>
    	*/

    	//This arrayToDataTable can also set data to table.
    	/*
        var data = google.visualization.arrayToDataTable([
          [{label: 'City', id: 'City', type: 'string'}, {label: 'count', id: 'count', type: 'number'}],
           <c:forEach var="customers" items="${customers}">
                ['${customers.city}', 1], 
           </c:forEach>
        ]);*/
        
        //DataTable can also insert data by method addRows().
        /*
        data.addRows([
          ['Mushrooms', 3],
          ['Onions', 1],
          ['Olives', 1],
          ['Zucchini', 1],
          ['Pepperoni', 2]
        ]);
        */

        // Set chart options
        var options = {'title':'Customers from city.',
                       'width':400,
                       'height':300};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_city'));
        chart.draw(data, options);
      }
      
      function drawGenderStatistic() {
          // Create the data table.
          var data = new google.visualization.DataTable();
          data.addColumn('string', 'Gender');
          data.addColumn('number', 'count');
          
          <c:forEach items="${genderStatistic}" var="genderStatistic">
          	data.addRow(['${genderStatistic.key}', ${genderStatistic.value}]);
    	  </c:forEach>

          // Set chart options
          var options = {'title':'Customers\'s gender.',
                         'width':400,
                         'height':300};

          // Instantiate and draw our chart, passing in some options.
          var chart = new google.visualization.PieChart(document.getElementById('chart_gender'));
          chart.draw(data, options);
        }
    </script>
  </head>

  <body>
    <!--Div that will hold the pie chart-->
    <div id="chart_city"></div>
    <div id="chart_gender"></div>
    <c:if test="${not empty customers}">
    	<table border="1">
    		<tr><th>ID</th><th>FirstName</th><th>LastName</th><th>Gender</th><th>Street</th><th>City</th></tr>
			<c:forEach items="${customers}" var="customer">
    			<tr>
			    	<td><c:out value="${customer.resourceId}"></c:out></td>
			    	<td><c:out value="${customer.firstName}"></c:out></td>
			    	<td><c:out value="${customer.lastName}"></c:out></td>
			    	<td><c:out value="${customer.gender}"></c:out></td>
			    	<td><c:out value="${customer.street}"></c:out></td>
			    	<td><c:out value="${customer.city}"></c:out></td>
    			</tr>
			</c:forEach>
    	</table>
    </c:if>
    
  </body>
</html>