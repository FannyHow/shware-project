import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
  faHeart,
  faEnvelope,
  faBell,
  faList,
  faPlus,
  faComments,
  faHome,
  faUser,
  faUserPlus,
  faSignInAlt,
  faSignOutAlt,
} from "@fortawesome/free-solid-svg-icons";

library.add(faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt, faComments, faPlus, faList, faBell, faHeart, faEnvelope);

export { FontAwesomeIcon };
