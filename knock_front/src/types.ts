export interface ICategory {
  categoryId: string;
  categoryNm: string;
  movies: string[];
}

export interface ISearch {
  searchTitle: string;
  searchCategory: string;
}

export interface IFavUser {
  id: string;
  name: string;
}

export interface ICategoryLevelTwo {
  id: string;
  nm: string;
  parentNm: string;
  favoriteUsers: IFavUser[];
}

export interface IMovie {
  movieId: string;
  movieNm: string;
  openingTime: string;
  reservationLink: string[];
  posterBase64: string;
  directors: string[];
  actors: string[];
  companyNm: string;
  categoryLevelTwo: ICategoryLevelTwo[];
  runningTime: number;
  plot: string;
  favorites: IFavUser[];
  koficcode: string;
}

export interface ISubList {
  MOVIE: IMovie[];
  PERFORMING_ARTS: any;
  EXHIBITION: any;
}

export interface IUser {
  alarmTimings: string[];
  email: string;
  favoriteLevelOne: 'MOVIE' | 'PERFORMING_ARTS' | 'EXHIBITION';
  id: string;
  lastLoginTime: string;
  loginType: 'KAKAO' | 'GOOGLE' | 'NAVER' | 'GUEST';
  name: string;
  nickName: string;
  picture: string;
  role: string;
  subscribeList: {
    MOVIE: IMovie[];
    PERFORMING_ARTS: any;
    EXHIBITION: any;
  };
}
