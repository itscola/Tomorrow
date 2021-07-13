/**
  * Copyright 2021 json.cn 
  */
package tomorrow.tomo.guis.musicPlayer.json;
import java.util.List;

/**
 * Auto-generated: 2021-07-10 23:51:44
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class JsonRootBean {

    private int code;
    private String relatedVideos;
    private Playlist playlist;
    private String urls;
    private List<Privileges> privileges;
    private String sharedPrivilege;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setRelatedVideos(String relatedVideos) {
         this.relatedVideos = relatedVideos;
     }
     public String getRelatedVideos() {
         return relatedVideos;
     }

    public void setPlaylist(Playlist playlist) {
         this.playlist = playlist;
     }
     public Playlist getPlaylist() {
         return playlist;
     }

    public void setUrls(String urls) {
         this.urls = urls;
     }
     public String getUrls() {
         return urls;
     }

    public void setPrivileges(List<Privileges> privileges) {
         this.privileges = privileges;
     }
     public List<Privileges> getPrivileges() {
         return privileges;
     }

    public void setSharedPrivilege(String sharedPrivilege) {
         this.sharedPrivilege = sharedPrivilege;
     }
     public String getSharedPrivilege() {
         return sharedPrivilege;
     }

}