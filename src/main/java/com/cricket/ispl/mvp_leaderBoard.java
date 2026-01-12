package com.cricket.ispl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class mvp_leaderBoard {

	@JsonIgnore
    private boolean status;
	@JsonIgnore
    private String message;
	@JsonProperty("data")
    private Data data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        
    	@JsonProperty("top")
    	private List<Player> top;
    	@JsonProperty("list")
        private List<Player> list;
        @JsonIgnore
        private Meta meta;

        public List<Player> getTop() {
            return top;
        }

        public void setTop(List<Player> top) {
            this.top = top;
        }

        public List<Player> getList() {
            return list;
        }

        public void setList(List<Player> list) {
            this.list = list;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }

		@Override
		public String toString() {
			return "Data [top=" + top + ", list=" + list + ", meta=" + meta + "]";
		}
        
    }

    public static class Player {
        
    	@JsonProperty("pid")
    	private String playerId;
    	
    	@JsonProperty("ispl_id")
    	private String isplId;
    	
    	@JsonProperty("player_full_name")
        private String playerFullName;
    	
    	@JsonProperty("team_name")
        private String teamName;
    	
    	@JsonProperty("playing_role")
        private String playingRole;
    	
    	@JsonProperty("final_points")
        private double finalPoints;
        
        @JsonIgnore
        private String player_profile;
        
        @JsonProperty("position")
        private int position;

		public String getPlayerId() {
			return playerId;
		}

		public void setPlayerId(String playerId) {
			this.playerId = playerId;
		}

		public String getIsplId() {
			return isplId;
		}

		public void setIsplId(String isplId) {
			this.isplId = isplId;
		}

		public String getPlayerFullName() {
			return playerFullName;
		}

		public void setPlayerFullName(String playerFullName) {
			this.playerFullName = playerFullName;
		}

		public String getTeamName() {
			return teamName;
		}

		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}

		public String getPlayingRole() {
			return playingRole;
		}

		public void setPlayingRole(String playingRole) {
			this.playingRole = playingRole;
		}

		public double getFinalPoints() {
			return finalPoints;
		}

		public void setFinalPoints(double finalPoints) {
			this.finalPoints = finalPoints;
		}

		public String getPlayer_profile() {
			return player_profile;
		}

		public void setPlayer_profile(String player_profile) {
			this.player_profile = player_profile;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		@Override
		public String toString() {
			return "Player [playerId=" + playerId + ", isplId=" + isplId + ", playerFullName=" + playerFullName
					+ ", teamName=" + teamName + ", playingRole=" + playingRole + ", finalPoints=" + finalPoints
					+ ", player_profile=" + player_profile + ", position=" + position + "]";
		}

    }

    public static class Meta {
        private int total_players;
        private int total_rest;
        private int per_page;
        private int current_page;
        private int last_page;

        public int getTotal_players() {
            return total_players;
        }

        public void setTotal_players(int total_players) {
            this.total_players = total_players;
        }

        public int getTotal_rest() {
            return total_rest;
        }

        public void setTotal_rest(int total_rest) {
            this.total_rest = total_rest;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }
    }

	@Override
	public String toString() {
		return "mvp_leaderBoard [status=" + status + ", message=" + message + ", data=" + data + "]";
	}
    
}
