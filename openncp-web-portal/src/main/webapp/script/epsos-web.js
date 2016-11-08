/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
wicketAjaxBusy = function() {
    for (var c in Wicket.channelManager.channels) {
        if (Wicket.channelManager.channels[c].busy) {
            Wicket.Log.info("Channel " + c + " is busy");
            return true; 
        }
    }
    Wicket.Log.info("No channels are busy");
    return false;
}

