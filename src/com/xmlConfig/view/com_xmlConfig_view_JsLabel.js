
var nx, ny, nz;

com_xmlConfig_view_JsLabel = function() {

  this.onStateChange = function() {
    nx = this.getState().nx;
    ny = this.getState().ny;
    nz = this.getState().nz;
  };
};
