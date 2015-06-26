zk.procDelay = 350;
if (!zUtl.oldProgressbox) {
  zUtl.oldProgressbox = zUtl.progressbox;
  zUtl.progressbox = function (id, msg, mask, icon, _opts) {
    zUtl.oldProgressbox(id, msg, true, icon, _opts);
  }
}