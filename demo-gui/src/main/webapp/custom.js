var DC4CitiesDemo = angular.module('DC4CitiesDemo', ['ui.bootstrap']);

DC4CitiesDemo.controller('DC4CitiesCustomDemoController', [ '$http', '$scope', function($http, $scope) {
    var customDemoController = this;

    //init parameters
    customDemoController.debugmode = false;
    var splitsign = "_";


    //REST-Services
    var urlBase = '../demo-rest/rest/';
    var customdemoservice = urlBase + 'customdemoservice/';
    var testbedservice = urlBase + 'testbedservice';
    var dayprofileservice = urlBase + 'dayprofileservice/';
    var activityservice = urlBase + 'activityservice/';

  $scope.today = function() {
    customDemoController.date = new Date();
  };
  $scope.today();

  $scope.clear = function () {
    customDemoController.date = null;
  };

  // Disable weekend selection
//  $scope.disabled = function(date, mode) {
////    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
//    return null;
//  };

  $scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : new Date();
  };
//  $scope.toggleMin();
  $scope.maxDate = new Date(2020, 5, 22);

  $scope.open = function($event) {
    $scope.status.opened = true;
  };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };

  $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
  $scope.format = $scope.formats[0];

  $scope.status = {
    opened: false
  };

  var tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  var afterTomorrow = new Date();
  afterTomorrow.setDate(tomorrow.getDate() + 2);
  $scope.events =
    [
      {
        date: tomorrow,
        status: 'full'
      },
      {
        date: afterTomorrow,
        status: 'partially'
      }
    ];

  $scope.getDayClass = function(date, mode) {
    if (mode === 'day') {
      var dayToCheck = new Date(date).setHours(0,0,0,0);

      for (var i=0;i<$scope.events.length;i++){
        var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

        if (dayToCheck === currentDay) {
          return $scope.events[i].status;
        }
      }
    }

    return '';
  };


    customDemoController.init = function () {
        console.log("demo initalize");
        customDemoController.getTestbeds();
    }

    customDemoController.update = function () {
        console.log("demo update");
        customDemoController.renpercents = customDemoController.testbed.renewablePercentages;
        customDemoController.renpercent = customDemoController.renpercents[0];
        customDemoController.penalties = customDemoController.testbed.penalties;
        customDemoController.penalty = customDemoController.penalties[0];
        customDemoController.getActivities();
        customDemoController.getDayProfiles();
    }


    customDemoController.getTestbeds = function () {
            $http.get(testbedservice).success(function(data){
                customDemoController.testbeds = data;
                customDemoController.testbed = customDemoController.testbeds[0];
                customDemoController.update();
            	});
        };

    customDemoController.getActivities = function () {
            $http.get(activityservice + customDemoController.testbed.id).success(function(data){
                customDemoController.activities = data;
            	});
        };

    customDemoController.getDayProfiles = function () {
            $http.get(dayprofileservice+ customDemoController.testbed.id).success(function(data){
                customDemoController.sources = data;
                customDemoController.source = customDemoController.sources[0];
            	});
        };


    customDemoController.startDemo = function () {
        var paramobject = {};
        paramobject.testBedId = customDemoController.testbed.id;
        paramobject.renewablePercentage = customDemoController.renpercent;
        paramobject.penalty = customDemoController.penalty;
        paramobject.dayProfile = customDemoController.source;
        paramobject.startDate = $.format.date(customDemoController.date, 'yyyy-MM-dd');
        $scope.simulationInProgress = true;
        $http.post(customdemoservice, paramobject)
        	.success(function(data) {
        		$scope.simulationInProgress = false;
        		customDemoController.changeView('result');
        	})
        	.error(function () {
        		$scope.simulationInProgress = false;
        	});
    };

    customDemoController.changeView = function (view) {
    var url = "http://";
    var destination = "localhost"
    //"/asset/[asset code]/name/[dashboard name]?from=[start date yyyy-mm-dd]&to=[end date yyyy-mm-dd]";
    var assetName = customDemoController.testbed.energisAsset;
    var dashboardName = customDemoController.testbed.energisDashboard;
    var startdate = $.format.date(customDemoController.date, 'yyyy-MM-dd')
    var enddate = new Date(customDemoController.date.getTime());
    enddate.setDate(enddate.getDate() + 1);
    enddate = $.format.date(enddate, 'yyyy-MM-dd');

    url += destination;
    url += "/energiscloud/pages/#/dashboards";
    url += "/asset/";
    url += assetName;
    url += "/name/";
    url += dashboardName;
    url += "?from=";
    url += startdate;
    url += "&to=";
    url += enddate ;
            window.open(url,"_blank")
            //$location.path(view); // path not hash
        };


    customDemoController.init();
  }]);