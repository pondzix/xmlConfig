var geometryDim = {};
var defaultGeometryDim = {};
var geometry, box, scaleX, scaleY, scaleZ;
var boxList = {};
var wedgeList = {};

window.com_xmlConfig_view_jsComponent_JsGeometryComponent = function() {

  this.onStateChange = function() {
	  if(!this.getState().sizeChange){
		  clearComponents();
		  setGeometry(defaultGeometryDim, this.getState()) ;	    	
	      geometryDim.nx = defaultGeometryDim.nx;
	      geometryDim.ny = defaultGeometryDim.ny;
	      geometryDim.nz = defaultGeometryDim.nz;  
	  } else {
		  setGeometry(geometryDim, this.getState()) ;       
	      scaleX = geometryDim.nx / defaultGeometryDim.nx;
	      scaleY = geometryDim.ny / defaultGeometryDim.ny;
	      scaleZ = geometryDim.nz / defaultGeometryDim.nz;	    	
	      geometry.scale.set(scaleX, scaleY, scaleZ);	
	      updateBoxes();
	  }		
  };
};
function clearComponents(){
	geometryDim = {};
	defaultGeometryDim = {};
	boxList = {};
	wedgeList = {};
}
function setGeometry(geometry, state){
	geometry.nx = (state.nx) ? state.nx : '1';
	geometry.ny = (state.ny) ? state.ny : '1';
	geometry.nz = (state.nz) ? state.nz : '1';
}

function updateBoxes(){
	for(var b in window.parent.boxList){
		var boxItem = window.parent.boxList[b];
		calculateBoxLength(boxItem);
		setBoxScale(boxItem);
		setBoxPosition(boxItem);
	}
}
