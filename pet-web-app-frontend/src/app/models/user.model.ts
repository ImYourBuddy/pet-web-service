export class User {
  id?: bigint;
  username?: string;
  password?: string;
  firstName?: string;
  lastName?: string;
  created?: Date;
  banned?: boolean;
  deleted?: boolean;
  roles?: string[];
}
