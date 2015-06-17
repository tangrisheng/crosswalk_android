//document.addEventListener("deviceready", function() {
var Sensoro = function() {
	this._listener = {};
};

Sensoro.prototype = {
	constructor: this,
	addEvent: function(type, fn) {
		if (typeof type === "string" && typeof fn === "function") {
			if (typeof this._listener[type] === "undefined") {
				this._listener[type] = [fn];
			} else {
				this._listener[type].push(fn);
			}
		}
		return this;
	},

	fireEvent: function(type, args) {
		if (type && this._listener[type]) {
			var events = {
				type: type,
				target: this,
				args: args
			};
			var length = this._listener[type].length
			for (var start = 0; start < length; start += 1) {
				this._listener[type][start].call(this, events);
			}
		}
		return this;
	},

	removeEvent: function(type, key) {
		var listeners = this._listener[type];
		if (listeners instanceof Array) {
			if (typeof key === "function") {
				for (var i = 0, length = listeners.length; i < length; i += 1) {
					if (listeners[i] === key) {
						listeners.splice(i, 1);
						break;
					}
				}
			} else if (key instanceof Array) {
				for (var lis = 0, lenkey = key.length; lis < lenkey; lis += 1) {
					this.removeEvent(type, key[lenkey]);
				}
			} else {
				delete this._listener[type];
			}
		}
		return this;
	},

};

var SensoroInstance = new Sensoro();
function onNewBeacon(beacon) {
//	alert(beacon);
	SensoroInstance.fireEvent('newbeacon', beacon);
}

function onGoneBeacon(beacon) {
	console.log('onGoneBeacon');
	SensoroInstance.fireEvent('gonebeacon', beacon);
}

function onUpdateBeacon(beacons) {
	console.log('onUpdateBeacon');
	SensoroInstance.fireEvent('updatebeacons', beacons);
}
//});